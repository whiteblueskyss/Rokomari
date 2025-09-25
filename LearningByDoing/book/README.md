# Book CRUD API

A simple Spring Boot REST API for managing books with MySQL database.

## Project Setup

**Technology Stack:**

- Spring Boot 3.5.6
- Java 21
- MySQL
- Maven

**Dependencies:**

- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- MySQL Connector

## How to Run

1. **Prerequisites:**

   - Java 21 installed
   - MySQL server running
   - Create database: `CREATE DATABASE bookdb;`

2. **Run the application:**

   ```bash
   ./mvnw spring-boot:run
   ```

3. **Access the API:**
   - Base URL: `http://localhost:8080`
   - Test with Postman

## Project Structure

```
src/main/java/com/learn/book/
├── BookApplication.java          # Main application class
├── controller/
│   └── BookController.java       # REST API endpoints
├── model/
│   └── Book.java                 # Book entity
├── repository/
│   └── BookRepository.java       # Data access layer
└── service/
    └── BookService.java          # Business logic
```

## API Endpoints

### Book CRUD Operations

| Method   | Endpoint          | Description          | Request Body |
| -------- | ----------------- | -------------------- | ------------ |
| `GET`    | `/api/books`      | Get all books        | None         |
| `GET`    | `/api/books/{id}` | Get book by ID       | None         |
| `POST`   | `/api/books`      | Create a new book    | Book JSON    |
| `PUT`    | `/api/books/{id}` | Update existing book | Book JSON    |
| `DELETE` | `/api/books/{id}` | Delete book by ID    | None         |
