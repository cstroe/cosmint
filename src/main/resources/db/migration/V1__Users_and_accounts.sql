CREATE TABLE users (
    id   SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE accounts (
    id      SERIAL,
    name    VARCHAR(255),
    user_id INTEGER NOT NULL REFERENCES users ON DELETE CASCADE
);