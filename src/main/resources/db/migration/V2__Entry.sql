CREATE TABLE entry (
    id                  SERIAL PRIMARY KEY,
    value               NUMERIC(15,2) NOT NULL,
    currency_code       VARCHAR(3) NOT NULL,
    entry_type          TEXT,
    transaction_date    DATE NOT NULL,
    posted_date         DATE NOT NULL,
    description         TEXT,
    account_id          INTEGER NOT NULL REFERENCES account ON DELETE CASCADE
);