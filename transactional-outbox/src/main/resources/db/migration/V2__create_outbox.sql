CREATE TABLE outbox_events
(
    event_id   UUID PRIMARY KEY,
    event_type VARCHAR(255) NOT NULL,
    post_id    UUID         NOT NULL,
    processed  BOOLEAN          DEFAULT FALSE,
    created_at TIMESTAMP        DEFAULT CURRENT_TIMESTAMP
);