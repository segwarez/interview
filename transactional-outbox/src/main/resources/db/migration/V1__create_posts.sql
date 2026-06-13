CREATE TABLE posts
(
    id      UUID PRIMARY KEY,
    title   VARCHAR(255) NOT NULL,
    author  VARCHAR(255) NOT NULL,
    content TEXT         NOT NULL
);