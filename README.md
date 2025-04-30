# Digital Wallet

A Spring Boot application for managing digital wallets, supporting multiple currencies and transaction types.

## Prerequisites

- Java 21
- Gradle
- H2 Database

## Technology Stack

- Spring Boot 3.2.4
- Spring Security
- H2 Database
- JWT Authentication
- Lombok
- JUnit (for testing)

## Features

- Customer management with TCKN (Turkish Identification Number)
- Multi-currency wallet support (TRY, USD, EUR)
- Transaction management (deposits and withdrawals)
- Separate balances for shopping and withdrawals
- JWT-based authentication
- In-memory H2 database

## Database Schema

### Customer Table

- `id`: Unique identifier (BIGINT, auto-increment)
- `name`: Customer's first name (VARCHAR)
- `surname`: Customer's last name (VARCHAR)
- `tckn`: Turkish Identification Number (VARCHAR, unique)

### Wallet Table

- `id`: Unique identifier (BIGINT, auto-increment)
- `customer_id`: Reference to customer (BIGINT)
- `wallet_name`: Name of the wallet (VARCHAR)
- `currency`: Wallet currency (TRY, USD, EUR)
- `active_for_shopping`: Shopping activation status (BOOLEAN)
- `active_for_withdraw`: Withdrawal activation status (BOOLEAN)
- `balance`: Total balance (DECIMAL)
- `usable_balance`: Available balance (DECIMAL)

### Transaction Table

- `id`: Unique identifier (BIGINT, auto-increment)
- `wallet_id`: Reference to wallet (BIGINT)
- `amount`: Transaction amount (DECIMAL)
- `type`: Transaction type (DEPOSIT, WITHDRAW)
- `opposite_party_type`: Counter-party type (IBAN, PAYMENT)
- `opposite_party`: Counter-party identifier (VARCHAR)
- `status`: Transaction status (PENDING, APPROVED, DENIED)
- `created_at`: Transaction timestamp (TIMESTAMP)

## Configuration

The application runs on port 8082 and uses an H2 in-memory database. The H2 console is enabled and accessible for
development purposes.
