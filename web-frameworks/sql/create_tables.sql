CREATE TABLE IF NOT EXISTS books(
    id          UUID PRIMARY KEY,
    title       VARCHAR(255),
    author      VARCHAR(255),
    genre       VARCHAR(255),
    description VARCHAR(1000),
    published   BOOLEAN
);