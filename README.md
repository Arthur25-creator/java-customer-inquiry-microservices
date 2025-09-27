# Java Microservices Coding Exam

## Microservices Implemented

- **Customer Inquiry Service** (port `8081`)
  - `/api/v1/account/{customerNumber}` — GET (Search customer)

## Tech Stack

- Java 8
- Spring Boot 2.6
- H2 In-Memory DB
- JPA
- JUnit + Mockito for unit testing
- Postman for manual testing

## How to Run

```bash
cd account-service
mvn spring-boot:run

cd ../customer-service
mvn spring-boot:run
