#!/usr/bin/env bash
set -euo pipefail

DEBEZIUM_CONNECT_URL="${DEBEZIUM_CONNECT_URL:-http://localhost:8083}"

until curl -fsS "${DEBEZIUM_CONNECT_URL}/connectors" >/dev/null; do
  echo "Waiting for Kafka Connect at ${DEBEZIUM_CONNECT_URL}..."
  sleep 2
done

echo "Registering MySQL Debezium source connector..."
curl -fsS -X POST \
  -H "Content-Type: application/json" \
  --data @connectors/mysql-source.json \
  "${DEBEZIUM_CONNECT_URL}/connectors" | jq . || true

echo "Registering PostgreSQL Debezium JDBC sink connector..."
curl -fsS -X POST \
  -H "Content-Type: application/json" \
  --data @connectors/postgres-jdbc-sink.json \
  "${DEBEZIUM_CONNECT_URL}/connectors" | jq . || true

echo "Done. Current connectors:"
curl -fsS "${DEBEZIUM_CONNECT_URL}/connectors" | jq .