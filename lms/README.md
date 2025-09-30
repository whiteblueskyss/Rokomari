# Library Management System (LMS) - REST API

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-green.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.java.net/)
[![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-blue.svg)](https://www.postgresql.org/)
[![JPA](https://img.shields.io/badge/ORM-Hibernate%20JPA-red.svg)](https://hibernate.org/)
[![Custom Auth](https://img.shields.io/badge/Security-Custom%20JWT-brightgreen.svg)](https://jwt.io/)
[![JWT](https://img.shields.io/badge/Auth-JWT-yellow.svg)](https://jwt.io/)
[![Flyway](https://img.shields.io/badge/Migration-Flyway-purple.svg)](https://flywaydb.org/)

## Project Overview

A **Library Management System** built with Spring Boot, featuring advanced authentication, role-based authorization, comprehensive REST APIs, and production-ready database management. This project demonstrates learning of modern Java backend development with industry-standard practices.

## Key Learning Achievements

- **Advanced Spring Boot Architecture**: Layered architecture with proper separation of concerns
- **JPA/Hibernate**: Complex entity relationships with lazy loading optimization  
- **JWT Authentication**: Custom authentication system using JWT.
- **Database Migration**: Flyway-based schema versioning and management
- **DTO Pattern Implementation**: Clean API design with data transfer objects
- **Global Exception Handling**: Comprehensive error management with custom exceptions
- **Production Database**: PostgreSQL integration with proper indexing strategies

## System Architecture

### **Technology Stack**
- **Framework**: Spring Boot 3.5.6 with Java 21
- **Database**: PostgreSQL with Flyway migrations
- **ORM**: Hibernate JPA with optimized queries
- **Authentication**: JWT (JSON Web Tokens) with HTTP-only cookies
- **Validation**: Jakarta Bean Validation with custom constraints
- **Build Tool**: Maven with dependency management

### **Project Structure**
```
src/main/java/com/selim/lms/
├── LmsApplication.java              # Spring Boot main application
├── controllers/                     # REST API endpoints
│   ├── BookController.java         # Book management APIs
│   ├── AuthorController.java       # Author management APIs  
│   ├── UserController.java         # User management APIs
│   ├── ReviewController.java       # Review management APIs
│   └── LoginController.java        # Authentication endpoints
├── entities/                       # JPA entity classes
│   ├── User.java                   # User entity with role enum
│   ├── Book.java                   # Book entity with relationships
│   ├── Author.java                 # Author entity
│   ├── Review.java                 # Review entity
│   └── Role.java                   # Role enumeration (READER/ADMIN)
├── dto/                           # Data Transfer Objects
│   ├── BookDto.java               # Book response DTO
│   ├── BookCreateUpdateDto.java   # Book request DTO
│   ├── UserDto.java               # User response DTO
│   ├── AuthorDto.java             # Author response DTO
│   └── LoginRequest.java          # Authentication request DTO
├── services/                      # Business logic layer
│   ├── BookService.java           # Book business operations
│   ├── BookServiceImpl.java       # Book service implementation
│   ├── UserService.java           # User business operations
│   └── UserServiceImpl.java       # User service implementation
├── repos/                         # Data access repositories
│   ├── BookRepository.java        # Book JPA repository
│   ├── UserRepository.java        # User JPA repository
│   ├── AuthorRepository.java      # Author JPA repository
│   └── ReviewRepository.java      # Review JPA repository
├── exceptions/                    # Exception handling
│   ├── GlobalExceptionHandler.java # Global exception handler
│   ├── NotFoundException.java      # Entity not found exception
│   ├── BusinessValidationException.java # Business logic exceptions
│   └── UnauthorizedAccessException.java # Authentication exceptions
└── utils/                        # Utility classes
    ├── JwtTokenUtil.java         # JWT token operations
    └── AuthUtil.java             # Authentication utility methods
```

## Database Schema & Relationships

### **Entity Relationship Diagram**
```sql
Users (1) ──────── (*) Reviews (*) ──────── (1) Books
  │                                            │
  │                                            │
  └── Role (ENUM: READER/ADMIN)                │
                                               │
                              Authors (*) ─────┴──── (*) Books
                                     (Many-to-Many)
```

### **Database Tables**
```sql
-- USERS TABLE
users (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    email       VARCHAR(120) NOT NULL UNIQUE,
    role        VARCHAR(20) NOT NULL  -- READER | ADMIN
);

-- AUTHORS TABLE  
authors (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(120) NOT NULL
);

-- BOOKS TABLE
books (
    id            BIGSERIAL PRIMARY KEY,
    title         VARCHAR(200) NOT NULL,
    genre         VARCHAR(80),
    publish_date  DATE
);

-- REVIEWS TABLE (One-to-Many: User -> Reviews, Book -> Reviews)
reviews (
    id        BIGSERIAL PRIMARY KEY,
    user_id   BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    book_id   BIGINT NOT NULL REFERENCES books(id) ON DELETE CASCADE,
    review    TEXT
);

-- JUNCTION TABLE (Many-to-Many: Books <-> Authors)
book_authors (
    book_id    BIGINT NOT NULL REFERENCES books(id) ON DELETE CASCADE,
    author_id  BIGINT NOT NULL REFERENCES authors(id) ON DELETE CASCADE,
    PRIMARY KEY (book_id, author_id)
);
```

### **Database Indexing Strategy**
```sql
-- Performance optimization indexes
CREATE INDEX idx_reviews_book_id ON reviews(book_id);
CREATE INDEX idx_reviews_user_id ON reviews(user_id);
CREATE INDEX idx_book_title ON books(title);
CREATE INDEX idx_author_name ON authors(name);
```

## Authentication & Authorization System

### **JWT Implementation**
- **Token Generation**
- **Token Storage**
- **Token Validation**
- **Expiry Management**

### **Role-Based Access Control**
- **READER Role**: Can view books, authors, and create reviews
- **ADMIN Role**: Full CRUD operations on all entities
- **Authorization Checks**: Method-level authorization in controllers

### **Security Features**
- **HTTP-Only Cookies**: Prevents client-side token access
- **Token Validation**: Comprehensive token verification


## Complete API Documentation

### **Authentication Endpoints**

| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|-------------|----------|
| `POST` | `/api/auth/login` | User login with JWT | LoginRequest JSON | JWT token + user info |

### **User Management APIs**

| Method | Endpoint | Description | Authorization | Request Body | Response |
|--------|----------|-------------|---------------|-------------|----------|
| `POST` | `/api/users` | Create new user | None | UserCreateUpdateDto JSON | User object |
| `GET` | `/api/users` | Get all users | None | None | List<User> |
| `GET` | `/api/users/{id}` | Get user by ID | None | None | User object |
| `PUT` | `/api/users/{id}` | Update user | None | UserCreateUpdateDto JSON | User object |
| `DELETE` | `/api/users/{id}` | Delete user | None | None | Status code 204 |

### **Book Management APIs**

| Method | Endpoint | Description | Authorization | Request Body | Response |
|--------|----------|-------------|---------------|-------------|----------|
| `POST` | `/api/books` | Create new book | ADMIN only | BookCreateUpdateDto JSON | Book object |
| `GET` | `/api/books` | Get all books | None | None | List<Book> |
| `GET` | `/api/books/{id}` | Get book by ID | None | None | Book object |
| `PUT` | `/api/books/{id}` | Update book | ADMIN only | BookCreateUpdateDto JSON | Book object |
| `DELETE` | `/api/books/{id}` | Delete book | ADMIN only | None | Status code 204 |

### **Author Management APIs**

| Method | Endpoint | Description | Authorization | Request Body | Response |
|--------|----------|-------------|---------------|-------------|----------|
| `POST` | `/api/authors` | Create new author | ADMIN only | AuthorCreateUpdateDto JSON | Author object |
| `GET` | `/api/authors` | Get all authors | None | None | List<Author> |
| `GET` | `/api/authors/{id}` | Get author by ID | None | None | Author object |
| `PUT` | `/api/authors/{id}` | Update author | ADMIN only | AuthorCreateUpdateDto JSON | Author object |
| `DELETE` | `/api/authors/{id}` | Delete author | ADMIN only | None | Status code 204 |

### **Review Management APIs**

| Method | Endpoint | Description | Authorization | Request Body | Response |
|--------|----------|-------------|---------------|-------------|----------|
| `POST` | `/api/reviews` | Create new review | Authenticated | ReviewCreateUpdateDto JSON | Review object |
| `GET` | `/api/reviews` | Get all reviews | None | None | List<Review> |
| `GET` | `/api/reviews/{id}` | Get review by ID | None | None | Review object |
| `GET` | `/api/reviews/book/{bookId}` | Get reviews by book | None | None | List<Review> |
| `PUT` | `/api/reviews/{id}` | Update review | Owner/ADMIN | ReviewCreateUpdateDto JSON | Review object |
| `DELETE` | `/api/reviews/{id}` | Delete review | Owner/ADMIN | None | Status code 204 |


### **4. Database Migration Management**
```sql
-- Flyway migration: V1__init.sql
-- Versioned schema evolution
-- Rollback capabilities
-- Environment-specific migrations
```

## Performance & Optimization

### **Database Optimization**
- **Lazy Loading**: Optimized entity relationships to prevent N+1 queries
- **Indexing Strategy**: Strategic indexes on frequently queried columns

### **API Performance**
- **DTO Conversion**: Efficient entity-to-DTO mapping
- **Response Optimization**: Minimal data transfer with focused DTOs

### **Exception Management**
- **Custom Exception Hierarchy**: Domain-specific exception types
- **Global Exception Handling**: Centralized error processing
- **Meaningful Error Messages**: User-friendly error responses
- **HTTP Status Code Accuracy**: Proper REST status code usage
---

*This Library Management System demonstrates  learning in Spring Boot development, showcasing authentication, complex database relationships, and good architectural patterns.*