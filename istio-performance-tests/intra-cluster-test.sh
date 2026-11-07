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

case "$REQUEST_PATH" in
  server|/server) REQUEST_PATH=/server ;;
  error|/error) REQUEST_PATH=/error ;;
  *)
    echo "Error: route must be /server or /error." >&2
    echo "Usage: $0 [/server|/error] [COUNT]" >&2
    exit 2
    ;;
esac

if ! [[ "$COUNT" =~ ^[1-9][0-9]*$ ]]; then
  echo "Error: request count must be a positive integer." >&2
  echo "Usage: $0 [/server|/error] [COUNT]" >&2
  exit 2
fi

command -v kubectl >/dev/null || { echo "Error: kubectl is required." >&2; exit 1; }
command -v curl >/dev/null || { echo "Error: curl is required." >&2; exit 1; }
kubectl -n "$GATEWAY_NAMESPACE" get service "$GATEWAY_SERVICE" >/dev/null

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
  echo "Error: could not start port-forward to $GATEWAY_SERVICE." >&2
  cat "$PORT_FORWARD_LOG" >&2
  exit 1
fi

URL="https://$INGRESS_HOST:$LOCAL_PORT$REQUEST_PATH"
printf 'Ingress: %s\nPath: %s\nRequests: %s\n\n' "$URL" "$REQUEST_PATH" "$COUNT"
printf 'Note: TLS certificate verification is skipped for this self-signed demo certificate.\n\n'

for ((i = 1; i <= COUNT; i++)); do
  curl_status=0
  result=$(curl --resolve "$INGRESS_HOST:$LOCAL_PORT:127.0.0.1" \
    --insecure --silent --show-error \
    --connect-timeout 3 --max-time 10 \
    --output /dev/null \
    --write-out '%{http_code} %{time_total}' \
    "$URL" 2>/dev/null) || curl_status=$?

  if [[ -z "$result" ]]; then
    result="000 0.000000"
  fi

  if [[ "$curl_status" -ne 0 ]]; then
    elapsed=${result##* }
    if ! [[ "$elapsed" =~ ^[0-9]+([.][0-9]+)?$ ]]; then
      elapsed="0.000000"
    fi
    result="000 $elapsed"
  fi

  printf '%s\n' "$result"
done |
awk -v expected="$COUNT" '
  {
    if (NF != 2 || $1 !~ /^[0-9][0-9][0-9]$/ || $2 !~ /^[0-9]+([.][0-9]+)?$/) {
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

    if (valid_count == 1 || milliseconds < minimum) minimum = milliseconds
    if (valid_count == 1 || milliseconds > maximum) maximum = milliseconds

    printf "call %04d | HTTP %s | %.3f ms\n", valid_count, status, milliseconds
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

    printf "\nSummary (%d requests): min=%.3f ms | avg=%.3f ms | p50=%.3f ms | p95=%.3f ms | max=%.3f ms\n", \
      valid_count, minimum, total / valid_count, median, durations[p95_index], maximum

    printf "HTTP status counts:"
    for (i = 0; i <= 999; i++) {
      status = sprintf("%03d", i)
      key = "s" status
      if (status_counts[key]) printf " HTTP %s=%d", status, status_counts[key]
    }
    printf "\n"
    if (transport_failures > 0) {
      printf "Error: %d curl transfer(s) failed; their status is reported as HTTP 000.\n", transport_failures > "/dev/stderr"
      exit 1
    }
  }
'