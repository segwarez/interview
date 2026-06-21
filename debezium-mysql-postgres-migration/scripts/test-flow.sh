#!/usr/bin/env bash
set -euo pipefail

echo "Source rows in MySQL:"
docker compose exec -T mysql mysql -umysql -pmysql userdb -e "SELECT * FROM users ORDER BY id;"

echo "Target rows in PostgreSQL:"
docker compose exec -T postgres psql -U postgres -d userdb -c 'SELECT * FROM users ORDER BY id;'

echo "Creating, updating and deleting rows in MySQL..."
docker compose exec -T mysql mysql -umysql -pmysql userdb <<'SQL'
INSERT INTO users (email, first_name, last_name, status)
VALUES ('kamil.sajdok@segwarez.com', 'Kamil', 'Sajdok', 'ACTIVE');

UPDATE users
SET status = 'LOCKED'
WHERE email = 'john.doe@test.com';

DELETE FROM users
WHERE email = 'alice.brown@test.com';
SQL

echo "Wait a few seconds, then check PostgreSQL:"
sleep 5
docker compose exec -T postgres psql -U postgres -d userdb -c 'SELECT * FROM users ORDER BY id;'