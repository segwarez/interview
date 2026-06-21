# Debezium MySQL to PostgreSQL online migration

MySQL → Debezium MySQL Source Connector → Kafka → Debezium JDBC Sink Connector → PostgreSQL

## Usage

Setup:

```shell script
docker compose up -d
./scripts/register-connectors.sh
```

Run test flow:

```shell script
./scripts/test-flow.sh
```

List connectors:

```shell script
curl -s http://localhost:8083/connectors | jq .
```

Connector status:

```shell script
curl -s http://localhost:8083/connectors/mysql-users-source/status | jq .
curl -s http://localhost:8083/connectors/postgres-users-sink/status | jq .
```

Check MySQL:

```shell script
docker compose exec mysql mysql -umysql -pmysql userdb -e "SELECT * FROM users ORDER BY id;"
```

Check PostgreSQL:

```shell script
docker compose exec postgres psql -U postgres -d userdb -c 'SELECT * FROM users ORDER BY id;'
```