CREATE TABLE events
(
    id        UUID PRIMARY KEY,
    user_id   VARCHAR(100) NOT NULL,
    type      VARCHAR(100)             NOT NULL,
    timestamp TIMESTAMP WITH TIME ZONE NOT NULL,
    payload   JSONB
);