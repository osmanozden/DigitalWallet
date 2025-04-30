CREATE TABLE customer (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          name VARCHAR(100) NOT NULL,
                          surname VARCHAR(100) NOT NULL,
                          tckn VARCHAR(11) NOT NULL UNIQUE
);

CREATE TABLE wallet (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        customer_id BIGINT NOT NULL,
                        wallet_name VARCHAR(100) NOT NULL,
                        currency VARCHAR(3) NOT NULL CHECK (currency IN ('TRY', 'USD', 'EUR')),
                        active_for_shopping BOOLEAN DEFAULT FALSE,
                        active_for_withdraw BOOLEAN DEFAULT FALSE,
                        balance DECIMAL(19, 2) DEFAULT 0,
                        usable_balance DECIMAL(19, 2) DEFAULT 0,
                        FOREIGN KEY (customer_id) REFERENCES customer(id)
);

CREATE TABLE transaction (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             wallet_id BIGINT NOT NULL,
                             amount DECIMAL(19, 2) NOT NULL,
                             type VARCHAR(10) NOT NULL CHECK (type IN ('DEPOSIT', 'WITHDRAW')),
                             opposite_party_type VARCHAR(10) NOT NULL CHECK (opposite_party_type IN ('IBAN', 'PAYMENT')),
                             opposite_party VARCHAR(100) NOT NULL,
                             status VARCHAR(10) NOT NULL CHECK (status IN ('PENDING', 'APPROVED', 'DENIED')),
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             FOREIGN KEY (wallet_id) REFERENCES wallet(id)
);
