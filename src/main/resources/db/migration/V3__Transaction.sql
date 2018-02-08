CREATE TABLE transaction (
    id      SERIAL PRIMARY KEY
);

CREATE TABLE transaction_to_entry (
    id              SERIAL PRIMARY KEY,
    transaction_id  INTEGER NOT NULL,
    entry_id        INTEGER NOT NULL,
    CONSTRAINT tte_transaction_fk FOREIGN KEY(transaction_id) REFERENCES transaction(id),
    CONSTRAINT tte_entry_fk FOREIGN KEY(entry_id) REFERENCES entry(id)
);