# Banking Management System

A full-featured banking backend REST API built with Java 21 and Spring Boot. This project demonstrates real-world backend engineering practices including JWT authentication, role-based access control, transaction management, AOP logging, scheduled jobs, and API documentation.

---

## Tech Stack

- **Java 21**
- **Spring Boot 3.5.12**
- **Spring Security** — JWT based authentication and role-based access
- **Spring Data JPA** — database operations with PostgreSQL
- **Spring Validation** — request validation
- **Spring AOP** — automatic logging across all service methods
- **Spring Scheduler** — daily account statement job
- **Spring Actuator** — health and monitoring endpoints
- **PostgreSQL** — relational database
- **Lombok** — boilerplate reduction
- **Swagger / SpringDoc** — interactive API documentation

---

## Features

- User registration and login with JWT authentication
- Role-based access control — USER and ADMIN roles
- Create savings or current bank accounts
- Deposit and withdraw money
- Transfer funds between accounts with balance validation
- Full transaction history per account
- In-app notifications for every account operation
- Admin endpoints to view all users and accounts
- AOP-based automatic logging of all service method calls and execution time
- Daily scheduled job that logs account summaries at 9 AM
- Global exception handling with meaningful error responses
- Interactive API docs via Swagger UI

---

## Project Structure

```
src/main/java/com/aishwarya/banking/
├── aop/                  # AOP logging aspect
├── config/               # Security, JWT filter, Swagger config
├── controller/           # REST controllers
├── dto/                  # Request and response objects
├── entity/               # JPA entities
├── exception/            # Custom exceptions and global handler
├── repository/           # Spring Data JPA repositories
├── scheduler/            # Scheduled jobs
├── service/              # Business logic
└── util/                 # JWT utility
```

---

## Database Schema

```
users
  id, name, email, password, role, created_at

accounts
  id, account_number, balance, account_type, status, user_id, created_at

transactions
  id, type, amount, description, status, account_id, timestamp

notifications
  id, message, read, user_id, sent_at
```

---

## Getting Started

### Prerequisites

- Java 21
- Maven
- PostgreSQL

### Setup

**1. Clone the repository**

```bash
git clone https://github.com/aishwarya-ashok/banking-management-system.git
cd banking-management-system
```

**2. Create the database**

```sql
CREATE DATABASE banking_db;
```

**3. Update application.yml**

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/banking_db
    username: your_username
    password: your_password
```

**4. Run the application**

```bash
mvn spring-boot:run
```

**5. Open Swagger UI**

```
http://localhost:8080/swagger-ui.html
```

---

## API Endpoints

### Auth

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | /api/auth/register | Register a new user | Public |
| POST | /api/auth/login | Login and get JWT token | Public |

### Accounts

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | /api/accounts/create | Create a bank account | USER |
| GET | /api/accounts/me | Get my accounts | USER |
| POST | /api/accounts/deposit | Deposit money | USER |
| POST | /api/accounts/withdraw | Withdraw money | USER |
| POST | /api/accounts/transfer | Transfer to another account | USER |

### Transactions

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | /api/transactions/history | Get transaction history | USER |

### Notifications

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | /api/notifications | Get my notifications | USER |
| PUT | /api/notifications/{id}/read | Mark as read | USER |

### Admin

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | /api/admin/users | Get all users | ADMIN |
| GET | /api/admin/accounts | Get all accounts | ADMIN |

### Actuator

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | /actuator/health | Application health |
| GET | /actuator/metrics | Application metrics |

---

## How to Test

1. Open Swagger UI at `http://localhost:8080/swagger-ui.html`
2. Register a user using `POST /api/auth/register`
3. Login using `POST /api/auth/login` — copy the JWT token from the response
4. Click **Authorize** in Swagger UI and paste the token as `Bearer <token>`
5. Create an account, deposit money, and try a transfer

---

## Sample Request — Register

```json
POST /api/auth/register
{
  "name": "Aishwarya",
  "email": "aishwarya@example.com",
  "password": "password123"
}
```

## Sample Request — Transfer

```json
POST /api/accounts/transfer
{
  "fromAccountNumber": "1234567890",
  "toAccountNumber": "0987654321",
  "amount": 500.00
}
```

---

## Running Tests

```bash
mvn test
```

---

## Health Check

```
http://localhost:8080/actuator/health
```

---

## Author

**A Aishwarya**  
[LinkedIn](https://linkedin.com/in/aishwarya-a-858bb4202) · [GitHub](https://github.com/aishwarya-ashok)
