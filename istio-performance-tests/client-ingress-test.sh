#!/usr/bin/env bash
set -Eeuo pipefail
export LC_ALL=C

NAMESPACE="${NAMESPACE:-perf-test}"
CLIENT_LABEL="${CLIENT_LABEL:-app=perf-client}"
CONTAINER="${CONTAINER:-perf-client}"
GATEWAY_NAMESPACE="${GATEWAY_NAMESPACE:-istio-ingress}"
GATEWAY_SERVICE="${GATEWAY_SERVICE:-istio-ingressgateway}"
INGRESS_HOST="${INGRESS_HOST:-api.segwarez.com}"
INGRESS_PORT="${INGRESS_PORT:-443}"
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

if ! [[ "$INGRESS_PORT" =~ ^[1-9][0-9]*$ ]] || (( INGRESS_PORT > 65535 )); then
  printf 'Error: INGRESS_PORT must be between 1 and 65535.\n' >&2
  exit 2
fi

command -v kubectl >/dev/null || { printf 'Error: kubectl is required.\n' >&2; exit 1; }

kubectl -n "$NAMESPACE" wait --for=condition=Ready pod -l "$CLIENT_LABEL" --timeout=60s >/dev/null
POD="$(kubectl -n "$NAMESPACE" get pod -l "$CLIENT_LABEL" --field-selector=status.phase=Running -o jsonpath='{.items[0].metadata.name}')"
if [[ -z "$POD" ]]; then
  printf 'Error: no running client pod found.\n' >&2
  exit 1
fi

if [[ "$HTTP_VERSION" == 2 ]] && ! kubectl -n "$NAMESPACE" exec "$POD" -c "$CONTAINER" -- curl --version | grep -Eq '(^|[[:space:]])HTTP2([[:space:]]|$)'; then
  printf 'Error: curl in container %s does not support HTTP/2.\n' "$CONTAINER" >&2
  exit 1
fi

if [[ -n "${INGRESS_ADDRESS:-}" ]]; then
  TARGET_ADDRESS="$INGRESS_ADDRESS"
  TARGET_MODE=external
else
  TARGET_ADDRESS="$(kubectl -n "$GATEWAY_NAMESPACE" get service "$GATEWAY_SERVICE" -o jsonpath='{.spec.clusterIP}')"
  TARGET_MODE=cluster-service
fi

if [[ -z "$TARGET_ADDRESS" || "$TARGET_ADDRESS" == None ]]; then
  printf 'Error: could not determine ingress address. Set INGRESS_ADDRESS.\n' >&2
  exit 1
fi

printf 'Client pod: %s\nIngress host: %s\nTarget address: %s:%s (%s)\nHTTP version requested: HTTP/%s\nRequests: %s\nPath: %s\n\n' \
  "$POD" "$INGRESS_HOST" "$TARGET_ADDRESS" "$INGRESS_PORT" "$TARGET_MODE" "$HTTP_VERSION" "$COUNT" "$REQUEST_PATH"

kubectl -n "$NAMESPACE" exec "$POD" -c "$CONTAINER" -- \
  sh -c '
    count=$1
    host=$2
    port=$3
    address=$4
    path=$5
    version=$6
    http_option=$7
    url="https://$host:$port$path"
    i=1
    while [ "$i" -le "$count" ]; do
      curl_status=0
      result=$(curl --resolve "$host:$port:$address" "$http_option" \
        --insecure --silent --show-error \
        --connect-timeout 3 --max-time 10 \
        --output /dev/null \
        --write-out "%{http_code} %{time_total} %{http_version}" \
        "$url" 2>/dev/null) || curl_status=$?

      if [ -z "$result" ]; then result="000 0.000000 0"; fi
      set -- $result
      status=${1:-000}
      elapsed=${2:-0.000000}
      actual_version=${3:-0}
      if [ "$curl_status" -ne 0 ]; then status=000; fi
      printf "%s %s %s\n" "$status" "$elapsed" "$actual_version"
      i=$((i + 1))
    done
  ' client-ingress-test "$COUNT" "$INGRESS_HOST" "$INGRESS_PORT" "$TARGET_ADDRESS" "$REQUEST_PATH" "$HTTP_VERSION" "$CURL_HTTP_OPTION" |
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
      printf "Error: %d request(s) failed before receiving an HTTP response.\n", transport_failures > "/dev/stderr"
      exit 1
    }
  }
'