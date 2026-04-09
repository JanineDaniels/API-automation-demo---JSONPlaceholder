# API Test Automation – JSONPlaceholder
An automated API test suite for the JSONPlaceholder REST API, created as part of a take-home assessment for an SDET position.

## Overview
This project contains an automated API test suite for the JSONPlaceholder REST API:

[https://jsonplaceholder.typicode.com/](https://jsonplaceholder.typicode.com/)

This project demonstrates a structured approach to API test automation, focusing on maintainability, validation accuracy, and handling of real-world API behaviors.

---

## Tech Stack

- Java  
- RestAssured  
- JUnit 5  
- Maven  

I’m using Eclipse Temurin (OpenJDK) for stability and compatibility with modern Java tooling.

---

## Project Structure

```
src/
  test/
    java/
      base/        # Base test setup
      client/      # API client layer
      models/      # POJOs
      tests/       # Test classes (GET, POST, PUT, DELETE)
      utils/       # Test data factory
      constants/   # Shared constants (e.g., endpoints, timeouts, configuration values)
    resources/
      post-schema.json
      posts-schema.json
      empty-schema.json
```

---

## Setup Instructions

### Prerequisites
- Java 11+
- Maven installed

### Install Dependencies
Run the following command:

```bash
mvn clean install
```

---

## Running Tests

Run all tests using:

```bash
mvn test
```

Run a specific test class:

```bash
mvn -Dtest=PostUpdateTests test
```

---

## Test Design & Approach

### Scope
- `/posts` endpoint
- CRUD operations (GET, POST, PUT, DELETE)

---

### Key Validations
- Status codes (200, 201, 404)
- Response schema validation
- Response body content (data integrity)
- Content-Type (application/json)
- Response time (< 2 seconds)

---

### Test Coverage
The suite covers CRUD operations for the `/posts` endpoint:

- GET /posts  
- GET /posts/{id}  
- POST /posts  
- PUT /posts/{id}  
- DELETE /posts/{id}  

Both **positive and negative scenarios** are included.

---

## Test Flow

- Tests are primarily atomic and independent.
- End-to-end flows were considered but not implemented to avoid coupling between test cases.

### Key Design Decisions

#### 1. Parameterized Testing
Parameterized tests were used to avoid duplication and support scalability.

I included the test case ID as a parameter to maintain traceability between automated tests and the test plan, and to improve readability in test reports.

Alternatively, scenarios such as valid and invalid IDs could be separated into individual test methods depending on team conventions.

---

#### 2. Test Data Management
A test data factory is used to generate request payloads.  
This avoids hardcoded values and improves maintainability.

---

#### 3. Schema Validation
JSON schema validation is used to verify response structure:

- `post-schema.json` → single object  
- `posts-schema.json` → list of objects  
- `empty-schema.json` → empty response `{}`  

This ensures contract validation beyond simple field checks.

---

#### 4. Handling Mock API Behavior

This project uses the JSONPlaceholder API, which is a mock REST service. As a result, certain behaviors differ from those expected in a production-grade API.

#### Observed Behavior

- Data is not persisted after `POST`, `PUT`, or `DELETE` requests  
- `DELETE` requests do not actually remove resources  
- Some invalid requests return successful responses (e.g., `POST` → `201`, `DELETE` → `200`)  
- `PUT` requests for non-existent resources may return inconsistent responses (e.g., `200` or `500`, instead of `404`)  
- Error responses are not always consistently structured (e.g., non-JSON responses for `500` errors)

#### Testing Approach

Test assertions are based on **actual observed API behavior** rather than strict REST expectations.

- Positive scenarios validate status codes, response structure, and data integrity  
- Negative scenarios account for inconsistent behavior by allowing multiple acceptable outcomes where necessary  
- JSON validation is performed only when the response format is appropriate (e.g., `application/json`)  

Where behavior deviates from real-world standards, this is explicitly documented in the test cases and comments.

#### Note on Real-World Expectations

In a production environment, stricter and more consistent behavior would be expected, such as:

- Proper use of HTTP status codes (e.g., `404 Not Found` for non-existent resources)  
- Consistent error response formats  
- Reliable data persistence  

This distinction is considered in the test design to ensure clarity between **mock API limitations** and **real-world testing standards**.

#### 5. Validation Strategy

Each test includes validation for:

- HTTP status codes  
- Response body structure (schema validation)  
- Request/response data integrity (POST/PUT)  
- Content-Type (application/json)  
- Response time (< 2s)

#### 6. Error Handling

Tests include safeguards to handle inconsistent API responses:

- JSON parsing is only performed when the response Content-Type is `application/json`
- Non-JSON responses (e.g., `500 Internal Server Error`) are handled gracefully
- Assertions are designed to avoid false failures caused by mock API limitations

---

## Notes on Test Behavior

Tests are designed to remain stable despite inconsistencies in the mock API:

- Assertions are adapted to allow multiple valid status codes where API behavior is inconsistent  
- JSON parsing and schema validation are only performed when the response is in a valid JSON format  
- Tests avoid relying on data persistence between requests  
- Each test is independent to prevent cascading failures caused by shared state assumptions  

These decisions ensure the test suite remains reliable while accurately reflecting the behavior of the system under test.

---


## AI Disclosure

AI tools (ChatGPT) were used to:
- Support debugging efforts   
- Suggest improvements to code structure
- Assist with schema analysis  
- Assist with schema validation setup  

All final implementation decisions and code were reviewed and adapted to ensure correctness and alignment with the assessment requirements.

---

## Conclusion

This project implements automated tests for the `/posts` endpoint, covering core CRUD operations, response validation, and negative scenarios.

It is designed with maintainability and readability in mind, using reusable components, schema validation, and parameterized tests where appropriate. Future improvements could include integration with CI/CD pipelines, enhanced reporting, and support for environment configuration.
