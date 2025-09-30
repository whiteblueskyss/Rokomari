

# E-Learning API

A simple Spring Boot REST API for an E-Learning platform with MySQL database.

## Project Background

This is my **first attempt** at building a complete Spring Boot REST API project. While the core functionality has been implemented, the project is **not fully complete** due to technical challenges encountered during development.

### Known Issues
- **Nested JSON Response Problem**: GET methods return deeply nested JSON structures in Postman, making the API responses difficult to consume
- **API Response Format**: Need to implement proper DTOs (Data Transfer Objects) to control response structure

### Learning Experience
This project served as a valuable learning experience in:
- Spring Boot application structure and configuration
- JPA entity relationships and their complexities
- REST API design challenges
- Database integration with MySQL
- Understanding the importance of proper response formatting in API design

## Features

- Course management
- Student management
- Lesson management
- Student enrollment
- Progress tracking

## Setup Instructions

### Prerequisites

- Java 17 or higher
- Maven
- MySQL database (via phpMyAdmin)

### Database Setup

1. Create a MySQL database named `elearning_db` in phpMyAdmin
2. Update database credentials in `src/main/resources/application.properties` if needed

### Running the Application

```bash
mvn spring-boot:run
```

The API will be available at `http://localhost:8080`

## API Endpoints

### Courses

- `POST /courses` - Create course
- `GET /courses` - List all courses
- `GET /courses/{id}` - Get course details
- `PUT /courses/{id}` - Update course
- `DELETE /courses/{id}` - Delete course

### Lessons

- `POST /courses/{id}/lessons` - Add lesson to course
- `GET /courses/{id}/lessons` - List lessons of a course

### Students

- `POST /students` - Create student
- `GET /students` - List all students
- `GET /students/{id}` - Get student details

### Enrollment & Progress

- `POST /courses/{id}/enroll` - Enroll student in course
- `GET /students/{id}/courses` - List student's enrolled courses
- `GET /students/{id}/progress` - Get student progress
- `POST /students/{id}/lessons/{lessonId}/complete` - Mark lesson complete
