# MediConnect - Digital Medical Appointment System

## Project Overview

MediConnect is a full-stack web application designed to digitalize medical appointment systems. It provides a comprehensive platform for managing medical consultations, doctor profiles, and prescription management with secure, user-friendly interfaces for different user roles.

## Objectives

- **For Super Admin**: Full system control with hardcoded credentials, complete access to all system functions and data
- **For Doctors**: Profile management, appointment viewing, prescription writing, and patient history access  
- **For Users/Patients**: Appointment booking, doctor profile viewing, medical history management, and doctor search functionality

## Key Features

### User Management
- User registration and secure authentication
- Role-based access control (Super Admin, Doctor, Patient)
- Profile management for doctors and patients

### Super Admin Functions
- Complete system access with hardcoded credentials
- Full control over doctor profiles, appointments, and prescriptions
- System-wide data management and oversight
- No API endpoints needed (direct database/system access)

### Doctor Functions  
- View and manage appointments
- Access patient history
- Write and manage prescriptions
- Profile updates and management

### Patient Functions
- Search and book appointments with doctors
- View doctor profiles and specializations
- Access medical history and prescriptions
- Manage personal profile information

## Technology Stack

### Backend
- **Framework**: Java Spring Boot
- **Security**: Spring Security with JWT
- **Database**: PostgreSQL
- **ORM**: Hibernate/Spring Data JPA
- **Caching**: Redis
- **Message Broker**: RabbitMQ
- **Server**: Apache Tomcat

### Development Tools
- **Version Control**: Git (GitHub)
- **Build Tool**: Maven
- **IDE**: VS Code

## System Architecture

```
Backend Services (Spring Boot) 
    ↓
Database (PostgreSQL) + Caching (Redis) + Message Broker (RabbitMQ)
    ↓
Security Layer (Spring Security)
```

## Database Schema

### Doctor Table
| Field | Type | Description | Notes |
|-------|------|-------------|-------|
| id | INT | Unique identifier | Manually assigned |
| name | VARCHAR(255) | Doctor's full name | Not null |
| email | VARCHAR(255) | Doctor's email | Unique |
| phone | VARCHAR(20) | Phone number | Optional |
| username | VARCHAR(255) | Login username | Unique |
| password | VARCHAR(255) | Hashed password | Secure |
| specializations | VARCHAR(255) | Medical specialization | Not null |
| visiting_days | VARCHAR(255) | Available days | String format |
| pic | VARCHAR(255) | Profile picture URL | Optional |

### Patient Table
| Field | Type | Description | Notes |
|-------|------|-------------|-------|
| id | INT | Unique identifier | Manually assigned |
| name | VARCHAR(255) | Patient's full name | Not null |
| email | VARCHAR(255) | Patient's email | Unique |
| phone | VARCHAR(20) | Phone number | Optional |
| username | VARCHAR(255) | Login username | Unique |
| password | VARCHAR(255) | Hashed password | Secure |
| pic | VARCHAR(255) | Profile picture URL | Optional |

### Appointment Table
| Field | Type | Description | Notes |
|-------|------|-------------|-------|
| id | INT | Unique identifier | Manually assigned |
| doctor_id | INT | Foreign key to Doctor | Required |
| patient_id | INT | Foreign key to Patient | Required |
| appointment_time | TIMESTAMP | Appointment date/time | Required |
| status | ENUM | scheduled/completed/canceled | Default: scheduled |
| problem_description | TEXT | Patient's issue description | Optional |
| prescription_id | INT | Foreign key to Prescription | Optional |

### Prescription Table
| Field | Type | Description | Notes |
|-------|------|-------------|-------|
| id | INT | Unique identifier | Manually assigned |
| patient_id | INT | Foreign key to Patient | Required |
| doctor_id | INT | Foreign key to Doctor | Required |
| prescription_data | JSONB | Prescription details in JSON | Medicines, dosage, etc. |

## API Documentation

### Authentication APIs
| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| POST | `/api/auth/login` | User login | `{"username": "string", "password": "string"}` | JWT token + user info |
| POST | `/api/auth/register` | User registration | `{"name": "string", "email": "string", "username": "string", "password": "string", "phone": "string"}` | Success message |
| POST | `/api/auth/logout` | User logout | - | Success message |
| GET | `/api/auth/profile` | Get current user profile | - | User profile data |

### Doctor APIs  
| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/doctor/profile` | Get doctor profile | - | Doctor profile |
| PUT | `/api/doctor/profile` | Update doctor profile | Doctor object | Updated profile |
| GET | `/api/doctor/appointments` | Get doctor's appointments | - | List of appointments |
| GET | `/api/doctor/appointments/upcoming` | Get upcoming appointments | - | Upcoming appointments |
| GET | `/api/doctor/appointments/past` | Get past appointments | - | Past appointments |
| PUT | `/api/doctor/appointments/{id}/status` | Update appointment status | `{"status": "completed/canceled"}` | Updated appointment |
| GET | `/api/doctor/patients/{id}/history` | Get patient history | - | Patient medical history |
| POST | `/api/doctor/prescriptions` | Create prescription | `{"patient_id": "int", "prescription_data": "json"}` | Prescription created |
| GET | `/api/doctor/prescriptions` | Get doctor's prescriptions | - | List of prescriptions |
<!-- | GET | `/api/doctor/patients/search` | Search patients | `?query=string` | Filtered patients | -->

### Patient APIs
| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/patient/profile` | Get patient profile | - | Patient profile |
| PUT | `/api/patient/profile` | Update patient profile | Patient object | Updated profile |
| GET | `/api/patient/doctors` | Get all doctors | - | List of doctors |
<!-- | GET | `/api/patient/doctors/search` | Search doctors | `?specialization=string&name=string` | Filtered doctors | -->
| GET | `/api/patient/doctors/{id}` | Get doctor details | - | Doctor profile |
| POST | `/api/patient/appointments` | Book appointment | `{"doctor_id": "int", "appointment_time": "timestamp", "problem_description": "string"}` | Appointment booked |
| GET | `/api/patient/appointments` | Get patient appointments | - | List of appointments |
| GET | `/api/patient/appointments/upcoming` | Get upcoming appointments | - | Upcoming appointments |
| GET | `/api/patient/appointments/past` | Get past appointments | - | Past appointments |
| PUT | `/api/patient/appointments/{id}/cancel` | Cancel appointment | - | Appointment canceled |
| GET | `/api/patient/prescriptions` | Get patient prescriptions | - | List of prescriptions |
| GET | `/api/patient/prescriptions/{id}` | Get prescription details | - | Prescription data |

<!--
### General APIs
| Method | Endpoint | Description | Request Body | Response |
|--------|----------|-------------|--------------|----------|
| GET | `/api/specializations` | Get all specializations | - | List of specializations |
| POST | `/api/upload/profile-picture` | Upload profile picture | Multipart file | File URL |
| GET | `/api/notifications` | Get user notifications | - | List of notifications |
| PUT | `/api/notifications/{id}/read` | Mark notification as read | - | Success message |
-->