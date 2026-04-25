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
