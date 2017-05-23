CREATE TABLE transactions (
    id      SERIAL PRIMARY KEY,
    effective_date TIMESTAMP DEFAULT now(),
    amount NUMERIC NOT NULL,
    name    VARCHAR(255) NOT NULL,
    description VARCHAR(1024),
    notes       TEXT,
    source_account_id INTEGER NOT NULL,
    target_account_id INTEGER NOT NULL,
    CONSTRAINT source_account_fk FOREIGN KEY(source_account_id) REFERENCES accounts(id),
    CONSTRAINT target_account_fk FOREIGN KEY(target_account_id) REFERENCES accounts(id)
);