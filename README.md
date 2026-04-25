# product-service

![CI](https://github.com/loza21wymlmn/product-service/actions/workflows/ci.yml/badge.svg)

A RESTful product microservice built with Spring Boot 3.

## Getting Started

### Prerequisites

* Java 17
* Maven

### Running the Application

To start the service locally, run the following command from the project root:

```bash
mvn spring-boot:run
```

The application will be available at `http://localhost:8080`.

## API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/products` | List all products |
| `GET` | `/products/{id}` | Get product by ID |
| `POST` | `/products` | Create a new product |
| `GET` | `/health` | Service health check |

### Example Request

Create a new product using `curl`:

```bash
curl -X POST http://localhost:8080/products \
  -H "Content-Type: application/json" \
  -d '{"name": "Wireless Mouse", "price": 29.99}'
```

## CI Pipeline

This project uses GitHub Actions for Continuous Integration. The pipeline is configured to automatically build the project and run all tests on every `push` and `pull_request` to the `main` branch.

## Project Structure

```text
src/main/java/com/example/productservice
├── controller
├── service
├── repository
└── model
```
This lab series focused on developing a RESTful Product Catalogue API using Spring Boot, starting from basic concepts in Lab 1 and progressing to a production-level system in Lab 2. In Lab 1, the project was created using Spring Initializr with dependencies such as Spring Web, Spring Data JPA, Validation, H2 Database, and Spring Boot Test, followed by organizing the application into a layered architecture consisting of Controller, Service, Repository, and Entity layers, where the Controller handles HTTP requests, the Service manages business logic, the Repository interacts with the database, and the Entity represents the database table, as illustrated in the architecture flow on page 6 of the lab document ; then a Product entity was implemented with fields id, name, and price using JPA annotations, a repository interface extending JpaRepository was created to handle CRUD operations, a service layer was developed to process data, and a REST controller was implemented to expose endpoints such as GET /products, GET /products/{id}, and POST /products, while configuring the H2 in-memory database in application.properties and testing the API using a browser, curl, or Postman, with optional seeding of sample data and writing unit tests using JUnit and Mockito to verify service functionality; in Lab 2, the project was extended to a more advanced and production-ready system by adding new fields such as stockQty and category to the entity, implementing the DTO pattern through ProductRequest and ProductResponse classes to separate internal models from API communication, and introducing full CRUD functionality including GET, POST, PUT, and DELETE endpoints under /api/v1/products, along with validation using annotations like @NotBlank, @DecimalMin, and @Min applied on DTOs to ensure correct input data, followed by implementing a custom ResourceNotFoundException and a GlobalExceptionHandler using @RestControllerAdvice to return structured ProblemDetail error responses compliant with modern API standards ; additionally, Swagger/OpenAPI was integrated to provide interactive API documentation accessible via a browser, and comprehensive integration testing was performed using MockMvc to verify correct HTTP status codes for both successful and failure scenarios, while also preparing a Postman collection for endpoint testing and configuring a GitHub Actions CI pipeline to automatically build and test the project, ensuring code reliability; overall, Lab 1 established the foundational architecture and basic API functionality, while Lab 2 enhanced the system with validation, error handling, documentation, and testing, resulting in a scalable, maintainable, and industry-standard backend application.


# lab 1

## REFLECTION

### 1. What is the purpose of the Service layer? Why should a Controller never call a Repository directly?

The Service layer contains the business logic of the application. It acts as a middle layer between the Controller and Repository. A Controller should not access the Repository directly because it breaks separation of concerns. Keeping business logic in the Service layer makes the application easier to maintain, test, and extend, and allows changes in data access without affecting the API layer.

---

### 2. Why do enterprise systems use layered architecture? What problems does it solve compared to writing all logic in one class?

Layered architecture separates responsibilities into different parts of the system (Controller, Service, Repository, etc.). This improves code organization and makes the system easier to understand and maintain. It solves problems like tightly coupled code, duplication of logic, and difficulty in testing. Without layers, all logic in one class becomes messy, hard to debug, and difficult to scale.

---

### 3. What advantages does Spring Data JPA provide over writing plain JDBC code? Give at least two specific examples.

Spring Data JPA reduces boilerplate code and simplifies database operations. For example, it automatically provides CRUD methods like `save()`, `findAll()`, and `deleteById()` without writing SQL. It also supports derived queries such as `findByNameContainingIgnoreCase()` where Spring generates the SQL automatically. This makes development faster and less error-prone compared to manual JDBC.

---

### 4. In your unit test, you used a mock for ProductRepository. Why is it important to isolate the class under test from its real dependencies?

Using a mock ensures that the test focuses only on the logic inside the class being tested (the Service). It avoids dependency on external systems like databases, making tests faster and more reliable. It also allows us to control the behavior of dependencies and test different scenarios easily, such as returning specific values or simulating missing data.

---

### 5. What would happen if spring.jpa.hibernate.ddl-auto were set to update instead of create-drop? When would you use each setting?

If `ddl-auto` is set to `update`, Hibernate will keep existing data and only modify the database schema when changes are made. If it is set to `create-drop`, the database schema is created at startup and dropped when the application stops. `update` is useful for development when you want to preserve data, while `create-drop` is useful for testing because it ensures a clean database every time the application runs.

# lab 2

# Reflection – Product Service

## 1. Why should the ProductRequest DTO carry the @Valid annotations instead of the Product entity itself?

We use @Valid on the ProductRequest DTO because that’s the data coming from the user (API input). It makes sense to validate it before it even reaches the system. The Product entity is more like the internal database model, so it shouldn’t be directly exposed or responsible for validation rules. Keeping validation in the DTO also makes the design cleaner and more separated.

---

## 2. What is the purpose of the Location header returned on a POST 201 Created response, and which HTTP specification mandates it?

The Location header tells the client where the newly created resource can be found. So after creating something, the client knows the exact URL to access it. This is part of the HTTP/1.1 standard (RFC 9110). It basically makes REST APIs more useful and predictable.

---

## 3. Explain the difference between @ControllerAdvice and @ExceptionHandler. When would you use each?

@ExceptionHandler is used inside a single controller to handle specific exceptions.

@ControllerAdvice is used when you want to handle exceptions globally across the whole application.

So basically, @ExceptionHandler = local handling, and @ControllerAdvice = global handling for all controllers.

---

## 4. In your MockMvc tests you used @Transactional on the test class. What would happen to the database state between tests if you removed this annotation?

If we remove @Transactional, the data created in one test would stay in the database and affect other tests. That would make tests unreliable and messy because they would depend on each other. With @Transactional, everything gets rolled back after each test, so every test starts fresh.

---

## 5. What does RFC 9457 define, and why does following it produce better APIs than returning a generic { error: 'something went wrong' } message?

RFC 9457 defines a standard format for API error responses called “Problem Details for HTTP APIs”.

Instead of returning a random error message, it gives a structured format (like type, title, status, and details). This makes it easier for developers and clients to understand what went wrong and debug issues properly.

---

## 6. What is the difference between an integration test (MockMvc) and a unit test (Mockito)? When is each approach preferable?

Unit tests (Mockito) test small pieces of code in isolation, usually by mocking dependencies. They are fast and good for checking business logic.

Integration tests (MockMvc) test the full flow, including controllers and HTTP requests, so they are closer to real usage but slower.

In general, unit tests are for logic, and integration tests are for checking the API works end-to-end.