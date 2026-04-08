#!/bin/sh

redis-server --appendonly yes & REDIS_PID=$!

until redis-cli ping; do
  sleep 1
done

redis-cli XGROUP CREATE WRITE_BEHIND_STREAM write_behind_group $ MKSTREAM

wait $REDIS_PID