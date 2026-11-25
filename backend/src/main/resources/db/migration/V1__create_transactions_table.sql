CREATE TABLE IF NOT EXISTS bank_transactions (
    id UUID PRIMARY KEY,
    bank_name VARCHAR(50) NOT NULL,
    transaction_date TIMESTAMP NOT NULL,
    description TEXT NOT NULL,
    amount NUMERIC(15, 2) NOT NULL,
    currency VARCHAR(10) NOT NULL,
    type VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
