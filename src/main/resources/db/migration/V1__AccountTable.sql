DROP TABLE IF EXISTS accounts;

CREATE TABLE IF NOT EXISTS accounts (
    id SERIAL NOT NULL PRIMARY KEY,
    account_holder_id TEXT NOT NULL,
    account_number TEXT NOT NULL,
    balance NUMERIC NOT NULL DEFAULT 0,
    UNIQUE (account_number)
);