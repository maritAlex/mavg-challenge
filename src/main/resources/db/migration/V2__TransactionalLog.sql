DROP TABLE IF EXISTS transactional_logs;

CREATE TABLE IF NOT EXISTS transactional_logs(
    id SERIAL NOT NULL PRIMARY KEY,
    transaction_id uuid NOT NULL,
    status TEXT NOT NULL,
    account_origin_id INTEGER NOT NULL,
    account_destination_id INTEGER NOT NULL,
    account_origin_balance NUMERIC NOT NULL,
    account_destination_balance NUMERIC NOT NULL,
    transaction_amount NUMERIC NOT NULL,
    created_time timestamptz NOT NULL DEFAULT now(),
    username TEXT NOT NULL
);

ALTER TABLE IF EXISTS transactional_logs
ADD CONSTRAINT account_origin_fk FOREIGN KEY (account_origin_id)
    REFERENCES accounts(id) ON DELETE restrict ON UPDATE cascade;

ALTER TABLE IF EXISTS transactional_logs
    ADD CONSTRAINT account_destination_fk FOREIGN KEY (account_destination_id)
        REFERENCES accounts(id) ON DELETE restrict ON UPDATE cascade;