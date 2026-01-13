# DiogoL0pes-wit-calculator
## Overview

This project implements a simple **calculator RESTful API** using **Spring Boot** and **Apache Kafka** for inter-module communication.

The application is divided into multiple Maven modules to ensure a clean separation of concerns and testability.

Supported operations:
- Sum
- Subtraction
- Multiplication
- Division

All operations support **arbitrary precision signed decimal numbers** using `BigDecimal`.

---

## Project Structure

The project is a **multi-module Maven project** composed of the following modules:

```
calculator-parent
├── common
├── calculator
└── rest
```

### Modules description

#### `common`
Shared module containing:
- Common DTOs (request/response objects)
- Shared constants (operation identifiers)

This module does **not** contain any Spring Boot application.

---

#### `calculator`
Spring Boot application responsible for:
- Calculator business logic
- Performing arithmetic operations
- Consuming calculation requests from Kafka
- Producing calculation results back to Kafka

This module **does not expose any HTTP endpoints**.

---

#### `rest`
Spring Boot application responsible for:
- Exposing the REST API
- Validating HTTP requests
- Sending calculation requests to Kafka
- Receiving calculation responses from Kafka and returning them to the client

---

## Architecture

```
Client (Postman)
     ↓ HTTP
REST Module
     ↓ Kafka (request)
Calculator Module
     ↓ Kafka (reply)
REST Module
     ↓ HTTP
Client
```

Apache Kafka is used as the communication mechanism between the `rest` and `calculator` modules.

---

## How to Run the Application

### 1️⃣ Start Kafka

Kafka must be running locally (for example via Docker).

```bash
docker compose up -d
```

---

### 2️⃣ Start the Calculator Module

The calculator service **must be started first**, as it listens for Kafka messages.

Run:
- `CalculatorApplication` (Spring Boot)

---

### 3️⃣ Start the REST Module

After the calculator module is running, start:
- `RestApplication` (Spring Boot)

---

## Testing the API with Postman

Once both modules are running, the API can be tested using **Postman** or any HTTP client.

### Example requests

#### Sum
```
GET http://localhost:8080/calculator/api/sum?a=1&b=2
```

Response:
```json
{
  "result": 3
}
```

#### Subtraction
```
GET http://localhost:8080/calculator/api/subtraction?a=5&b=3
```

#### Multiplication
```
GET http://localhost:8080/calculator/api/multiplication?a=4&b=6
```

#### Division
```
GET http://localhost:8080/calculator/api/division?a=10&b=4
```

---

## Running Unit Tests

The project includes **unit tests** for:
- Calculator business logic (`calculator` module)
- REST controller layer (`rest` module), with Kafka mocked

Tests can be executed using Maven:

```bash
mvn test
```

Or directly from the IDE:
```
Run As → Maven test
```

Kafka is **not required** to be running in order to execute unit tests.




