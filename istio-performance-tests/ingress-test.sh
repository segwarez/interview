#!/usr/bin/env bash
set -Eeuo pipefail
export LC_ALL=C

GATEWAY_NAMESPACE="${GATEWAY_NAMESPACE:-istio-ingress}"
GATEWAY_SERVICE="${GATEWAY_SERVICE:-istio-ingressgateway}"
INGRESS_HOST="${INGRESS_HOST:-api.segwarez.com}"
LOCAL_PORT="${LOCAL_PORT:-8443}"
REMOTE_PORT="${REMOTE_PORT:-443}"
REQUEST_PATH="${1:-/server}"
COUNT="${2:-1000}"
HTTP_VERSION="${HTTP_VERSION:-${3:-1.1}}"

case "$REQUEST_PATH" in
  server|/server) REQUEST_PATH=/server ;;
  error|/error) REQUEST_PATH=/error ;;
  *) printf 'Error: route must be /server or /error.\n' >&2; exit 2 ;;
esac

if ! [[ "$COUNT" =~ ^[1-9][0-9]*$ ]]; then
  printf 'Error: request count must be a positive integer.\n' >&2
  exit 2
fi

case "$HTTP_VERSION" in
  1.1) CURL_HTTP_OPTION=--http1.1 ;;
  2|2.0) HTTP_VERSION=2; CURL_HTTP_OPTION=--http2 ;;
  *) printf 'Error: HTTP version must be 1.1 or 2.\n' >&2; exit 2 ;;
esac

for port in "$LOCAL_PORT" "$REMOTE_PORT"; do
  if ! [[ "$port" =~ ^[1-9][0-9]*$ ]] || (( port > 65535 )); then
    printf 'Error: ports must be between 1 and 65535.\n' >&2
    exit 2
  fi
done

command -v kubectl >/dev/null || { printf 'Error: kubectl is required.\n' >&2; exit 1; }
command -v curl >/dev/null || { printf 'Error: curl is required.\n' >&2; exit 1; }
kubectl -n "$GATEWAY_NAMESPACE" get service "$GATEWAY_SERVICE" >/dev/null

if [[ "$HTTP_VERSION" == 2 ]] && ! curl --version | grep -Eq '(^|[[:space:]])HTTP2([[:space:]]|$)'; then
  printf 'Error: local curl does not support HTTP/2.\n' >&2
  exit 1
fi

PORT_FORWARD_LOG="$(mktemp)"
PORT_FORWARD_PID=""
cleanup() {
  if [[ -n "$PORT_FORWARD_PID" ]]; then
    kill "$PORT_FORWARD_PID" 2>/dev/null || true
    wait "$PORT_FORWARD_PID" 2>/dev/null || true
  fi
  rm -f "$PORT_FORWARD_LOG"
}
trap cleanup EXIT
trap 'exit 130' INT
trap 'exit 143' TERM

kubectl -n "$GATEWAY_NAMESPACE" port-forward \
  --address 127.0.0.1 \
  "service/$GATEWAY_SERVICE" \
  "$LOCAL_PORT:$REMOTE_PORT" >"$PORT_FORWARD_LOG" 2>&1 &
PORT_FORWARD_PID=$!

PORT_FORWARD_READY=0
ATTEMPT=0
while (( ATTEMPT < 50 )); do
  if grep -Fq "Forwarding from 127.0.0.1:$LOCAL_PORT -> $REMOTE_PORT" "$PORT_FORWARD_LOG"; then
    PORT_FORWARD_READY=1
    break
  fi
  if ! kill -0 "$PORT_FORWARD_PID" 2>/dev/null; then
    break
  fi
  sleep 0.1
  ATTEMPT=$((ATTEMPT + 1))
done

if [[ "$PORT_FORWARD_READY" != 1 ]]; then
  printf 'Error: could not start port-forward to %s.\n' "$GATEWAY_SERVICE" >&2
  cat "$PORT_FORWARD_LOG" >&2
  exit 1
fi

URL="https://$INGRESS_HOST:$LOCAL_PORT$REQUEST_PATH"
printf 'Ingress: %s\nPath: %s\nHTTP version requested: HTTP/%s\nRequests: %s\n\n' \
  "$URL" "$REQUEST_PATH" "$HTTP_VERSION" "$COUNT"
printf 'TLS certificate verification is disabled for this test certificate.\n\n'

for ((i = 1; i <= COUNT; i++)); do
  curl_status=0
  result="$(curl --resolve "$INGRESS_HOST:$LOCAL_PORT:127.0.0.1" "$CURL_HTTP_OPTION" \
    --insecure --silent --show-error \
    --connect-timeout 3 --max-time 10 \
    --output /dev/null \
    --write-out '%{http_code} %{time_total} %{http_version}' \
    "$URL" 2>/dev/null)" || curl_status=$?

  if [[ -z "$result" ]]; then result='000 0.000000 0'; fi
  read -r status elapsed actual_version <<<"$result"
  status="${status:-000}"
  elapsed="${elapsed:-0.000000}"
  actual_version="${actual_version:-0}"
  if [[ "$curl_status" -ne 0 ]]; then status=000; fi
  printf '%s %s %s\n' "$status" "$elapsed" "$actual_version"
done |
awk -v expected="$COUNT" -v requested_version="$HTTP_VERSION" '
  {
    if (NF != 3 || $1 !~ /^[0-9][0-9][0-9]$/ || $2 !~ /^[0-9]+([.][0-9]+)?$/ || $3 !~ /^(0|1\.1|2)$/) {
      printf "Error: invalid curl result on line %d: %s\n", NR, $0 > "/dev/stderr"
      invalid = 1
      next
    }
    status = $1
    milliseconds = ($2 + 0) * 1000
    durations[++valid_count] = milliseconds
    total += milliseconds
    status_counts["s" status]++
    if (status == "000") transport_failures++
    if (status != "000" && $3 != requested_version) protocol_mismatches++
    if (valid_count == 1 || milliseconds < minimum) minimum = milliseconds
    if (valid_count == 1 || milliseconds > maximum) maximum = milliseconds
    printf "call %04d | HTTP %s | HTTP/%s | %.3f ms\n", valid_count, status, $3, milliseconds
  }
  END {
    if (NR != expected) {
      printf "Error: expected %d curl results, received %d.\n", expected, NR > "/dev/stderr"
      invalid = 1
    }
    if (invalid || valid_count != expected) exit 1
    for (i = 2; i <= valid_count; i++) {
      value = durations[i]
      j = i - 1
      while (j >= 1 && durations[j] > value) {
        durations[j + 1] = durations[j]
        j--
      }
      durations[j + 1] = value
    }
    if (valid_count % 2 == 0) {
      middle = valid_count / 2
      median = (durations[middle] + durations[middle + 1]) / 2
    } else {
      median = durations[(valid_count + 1) / 2]
    }
    p95_index = int(valid_count * 0.95)
    if (p95_index < valid_count * 0.95) p95_index++
    if (p95_index < 1) p95_index = 1
    p99_index = int(valid_count * 0.99)
    if (p99_index < valid_count * 0.99) p99_index++
    if (p99_index < 1) p99_index = 1
    printf "\nSummary (%d requests): min=%.3f ms | avg=%.3f ms | p50=%.3f ms | p95=%.3f ms | p99=%.3f ms | max=%.3f ms\n", \
      valid_count, minimum, total / valid_count, median, durations[p95_index], durations[p99_index], maximum
    printf "HTTP status counts:"
    for (i = 0; i <= 999; i++) {
      status = sprintf("%03d", i)
      key = "s" status
      if (status_counts[key]) printf " HTTP %s=%d", status, status_counts[key]
    }
    printf "\n"
    if (protocol_mismatches > 0) {
      printf "Error: %d response(s) did not use requested HTTP/%s.\n", protocol_mismatches, requested_version > "/dev/stderr"
      exit 1
    }
    if (transport_failures > 0) {
      printf "Error: %d curl transfer(s) failed; their status is reported as HTTP 000.\n", transport_failures > "/dev/stderr"
      exit 1
    }
  }
'