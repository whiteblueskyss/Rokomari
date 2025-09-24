# E-Learning API

A simple Spring Boot REST API for an E-Learning platform with MySQL database.

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
