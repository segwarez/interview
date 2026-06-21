CREATE DATABASE IF NOT EXISTS userdb;

CREATE USER IF NOT EXISTS 'debezium'@'%' IDENTIFIED BY 'debezium';
GRANT SELECT, RELOAD, SHOW DATABASES, REPLICATION SLAVE, REPLICATION CLIENT, LOCK TABLES ON *.* TO 'debezium'@'%';
GRANT ALL PRIVILEGES ON userdb.* TO 'mysql'@'%';
FLUSH PRIVILEGES;

USE userdb;

CREATE TABLE IF NOT EXISTS users (
    id BIGINT NOT NULL AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    status VARCHAR(32) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    UNIQUE KEY uk_users_email (email)
);

INSERT INTO users (email, first_name, last_name, status) VALUES
    ('john.doe@test.com', 'John', 'Doe', 'ACTIVE'),
    ('jane.smith@test.com', 'Jane', 'Smith', 'ACTIVE'),
    ('alice.brown@test.com', 'Alice', 'Brown', 'LOCKED');