# Library Management System - Complete REST API

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://openjdk.java.net/)
[![MySQL](https://img.shields.io/badge/Database-MySQL-blue.svg)](https://www.mysql.com/)
[![Spring Security](https://img.shields.io/badge/Security-Spring%20Security-brightgreen.svg)](https://spring.io/projects/spring-security)
[![JdbcTemplate](https://img.shields.io/badge/Data%20Access-JdbcTemplate-red.svg)](https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/jdbc/core/JdbcTemplate.html)

## Project Overview

A **complete backend API** for a Library Management System built with Spring Boot. This project demonstrates comprehensive REST API development with proper database design, exception handling, validation, and relationship management.

##  Learning Objectives Achieved

- **REST API Design**: Built comprehensive CRUD operations following REST principles
- **Database Integration**: Used JdbcTemplate for efficient database operations
- **Relationship Management**: Implemented one-to-many and many-to-many relationships
- **Exception Handling**: Created global exception handling with custom exceptions
- **Data Validation**: Implemented input validation using Jakarta validation
- **Security Configuration**: Basic security setup for API protection

##  System Architecture

### **Core Entities**

#### **Book Entity**
```java
- id (Long) - Primary Key
- title (String) - Required, validated
- genre (String) - Optional classification
```

#### **Author Entity**
```java
- id (Long) - Primary Key  
- name (String) - Author's full name
```

#### **Review Entity**
```java
- id (Long) - Primary Key
- bookId (Long) - Foreign Key to Book
- userId (Long) - User identifier
- rating (Integer) - 1-5 scale, validated
- comment (String) - Review text
```

#### **BookAuthor Junction** (Many-to-Many)
```java
- bookId (Long) - Foreign Key to Book
- authorId (Long) - Foreign Key to Author
```

### **Project Structure**
```
src/main/java/com/learn/library/
├── LibraryApplication.java          # Main Spring Boot application
├── config/
│   └── SecurityConfig.java          # Security configuration
├── controller/                      # REST API endpoints
│   ├── BookController.java         # Book CRUD operations
│   ├── AuthorController.java       # Author CRUD operations
│   ├── ReviewController.java       # Review management
│   └── BookAuthorController.java   # Book-Author relationships
├── model/                          # Entity classes
│   ├── Book.java                   # Book entity with validation
│   ├── Author.java                 # Author entity
│   ├── Review.java                 # Review entity with constraints
│   └── BookAuthorDTO.java          # Data transfer object
├── repository/                     # Data access layer
│   ├── BookRepository.java         # Book database operations
│   ├── AuthorRepository.java       # Author database operations
│   ├── ReviewRepository.java       # Review database operations
│   └── BookAuthorRepository.java   # Junction table operations
├── service/                        # Business logic layer
│   ├── BookService.java            # Book business logic
│   ├── AuthorService.java          # Author business logic
│   ├── ReviewService.java          # Review business logic
│   └── BookAuthorService.java      # Relationship management
└── exception/                      # Exception handling
    ├── GlobalExceptionHandler.java # Centralized exception handling
    ├── BookNotFoundException.java  # Custom book exception
    ├── AuthorNotFoundException.java # Custom author exception
    └── InvalidReviewException.java # Custom review exception
```

##  Complete API Documentation

### **Book Management APIs**

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| `GET` | `/api/books` | Get all books | None | `List<Book>` |
| `GET` | `/api/books/{id}` | Get book by ID | None | `Book` or `404` |
| `POST` | `/api/books` | Create new book | `Book JSON` | `Book` (201) |
| `PUT` | `/api/books/{id}` | Update book | `Book JSON` | `Book` or `404` |
| `DELETE` | `/api/books/{id}` | Delete book | None | `204` or `404` |

### **Author Management APIs**

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| `GET` | `/api/authors` | Get all authors | None | `List<Author>` |
| `GET` | `/api/authors/{id}` | Get author by ID | None | `Author` or `404` |
| `POST` | `/api/authors` | Create new author | `Author JSON` | `Author` (201) |
| `PUT` | `/api/authors/{id}` | Update author | `Author JSON` | `Author` or `404` |
| `DELETE` | `/api/authors/{id}` | Delete author | None | `204` or `404` |

### **Review Management APIs**

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| `GET` | `/api/reviews/book/{bookId}` | Get all reviews for a book | None | `List<Review>` |
| `GET` | `/api/reviews/{id}` | Get review by ID | None | `Review` or `404` |
| `POST` | `/api/reviews` | Create new review | `Review JSON` | `Review` (201) |
| `PUT` | `/api/reviews/{id}` | Update review | `Review JSON` | `Review` or `404` |
| `DELETE` | `/api/reviews/{id}` | Delete review | None | `204` or `404` |

### **Book-Author Relationship APIs**

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| `POST` | `/api/book-author/assign` | Assign author to book | `BookAuthorDTO` | `201` |
| `DELETE` | `/api/book-author/remove` | Remove author from book | `BookAuthorDTO` | `204` |
| `GET` | `/api/book-author/book/{bookId}` | Get authors of a book | None | `List<Long>` |
| `GET` | `/api/book-author/author/{authorId}` | Get books by author | None | `List<Long>` |

## Technical Implementation Details

### **Data Access Layer (JdbcTemplate)**
- **SQL Operations**: Custom SQL queries.

### **Validation & Error Handling**
- **Input Validation**: Jakarta validation annotations (`@NotEmpty`, `@Min`, `@Max`)
- **Custom Exceptions**: Domain-specific exceptions for better error tracking
- **Global Exception Handler**: Centralized error handling with proper HTTP status codes
- **Meaningful Error Messages**: User-friendly error responses

### **Security Implementation**
- **Basic Security Configuration**: Spring Security setup
- **HTTP Security**: Basic in memory authentication and authorization

### **API Design Best Practices**
- **RESTful URLs**: Proper resource naming and HTTP methods
- **HTTP Status Codes**: Appropriate status codes for different scenarios
- **Response Consistency**: Standardized response formats
- **Content Negotiation**: JSON request/response handling

##  Features Implemented

### **1. Relationship Management**
- **One-to-Many**: Books → Reviews relationship
- **Many-to-Many**: Books ↔ Authors relationship
- **Junction Table**: Proper many-to-many implementation


### **2. Exception Handling Strategy**
- **Custom Exception Types**: `BookNotFoundException`, `AuthorNotFoundException`, `InvalidReviewException`
- **Global Handler**: `@ControllerAdvice` for centralized exception management
- **Validation Errors**: Proper handling of validation failures

## 📊 Database Design

### **Tables Structure**
```sql
books (id, title, genre)
authors (id, name)  
reviews (id, book_id, user_id, rating, comment)
book_author (book_id, author_id) -- Junction table
```

### **Relationships**
- **Books ← Reviews**: One-to-Many (One book can have multiple reviews)
- **Books ↔ Authors**: Many-to-Many (Books can have multiple authors, authors can write multiple books)

## Testing & Validation

### **API Testing**
- **Postman Collection**: Complete API testing suite
- **CRUD Operations**: All endpoints tested and verified
- **Error Scenarios**: Exception handling tested
- **Relationship Operations**: Junction table operations verified

### **Data Validation**
- **Input Constraints**: Required fields, data type validation
- **Business Rules**: Rating constraints (1-5), title requirements
- **Error Responses**: Proper validation error messages

## Key Learning Outcomes

### **Spring Boot**
- **Application Structure**: Proper layered architecture implementation
- **Dependency Injection**: `@Autowired` and constructor injection patterns
- **Configuration Management**: Properties and security configuration

### **Database Integration**
- **JdbcTemplate Proficiency**: Raw SQL operations and result mapping
- **Transaction Management**: Understanding database transactions
- **Relationship Modeling**: Complex entity relationships

### **REST API Excellence**
- **HTTP Protocol**: Proper use of HTTP methods and status codes
- **API Design**: RESTful resource design and URL patterns
- **Error Handling**: Comprehensive exception management strategy


## Technical Achievements

-  **Complete CRUD Operations** for all entities
-  **Complex Relationship Management** (One-to-Many, Many-to-Many)
- **Global Exception Handling** with custom exceptions
-  **Input Validation** with meaningful error messages
-  **Security Configuration** for API protection
-  **Clean Architecture** with proper layer separation

---

*This project demonstrates comprehensive understanding of Spring Boot development, REST API design, database integration, and enterprise-level coding practices.*
