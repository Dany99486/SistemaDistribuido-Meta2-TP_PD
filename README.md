# Distributed Programming Project (ISEC) 

## Overview

This project is a distributed programming assignment (ISEC) implementing a client-server architecture with a Java Spring Boot server and a Java client application.

The server provides authentication, event management, attendance code generation, and query endpoints. It uses JWT-based security and an SQLite database for persistence.

## Repository Structure

- `server/` - Spring Boot server project.
  - `src/main/java/pt/isec/pd/spring_boot/exemplo3/` - main application and controllers
  - `src/main/resources/application.properties` - configuration
  - `dataBase/` - database storage path used by server
- `cliente/` - Java client code (console or GUI client for interacting with server)
- `Executaveis/` - helper launch scripts

## Technologies

- Java 17+ (Spring Boot)
- Spring Security (HTTP Basic, JWT Resource Server)
- SQLite (embedded DB)
- Maven build system

## Features

### Server

- JWT authentication for protected routes
- Basic registration endpoint (`/register`)
- Login endpoint (`/login`) returns JWT
- Role-based behavior: `admin` vs `user` (client)
- Event creation and deletion (`/Evento`)
- Code generation (`/Codigo`)
- Attendance code submission and query
- Admin has special permissions for event management and code generation

### Client

- Interacts with server endpoints through HTTP requests
- Sends credentials and JWT tokens
- Provides command-driven or menu-driven flows for client/admin actions

## Default Credentials

When started for the first time, the server creates an admin account automatically:

- Email: `admin@isec.pt`
- Password: `123`
- Role: `admin`

## Server Endpoints (REST API)

### Public endpoints
- `POST /register` - Register a new user
  - Required params: `name`, `password`, `cc`, `email`
- `POST /login` - Login with basic auth credentials. Returns JWT string.

> Swagger UI is enabled through `springdoc-openapi-starter-webmvc-ui` and available at `/swagger-ui/index.html` (or `/v3/api-docs` for OpenAPI JSON), and the security config allows access to these paths.

### Protected endpoints (JWT required)
- `GET /role` - Returns role greeting
- `POST /Evento` - Create an event (admin only)
- `DELETE /Evento` - Delete an event (admin only)
- `GET /Consulta` - Query event or attendance data (user and admin logic differs)
- `PUT /Codigo` - Generate a code for an event (admin only)
- `POST /Codigo` - Submit a code for attendance (user)

## Quick Start

### 1) Start server

Open terminal in `server/` and run:

```bash
./mvnw spring-boot:run
```

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

The server runs on default Spring Boot port (usually `8080`).

### 2) Register and login

Register user example:

```bash
curl -X POST "http://localhost:8080/register?name=John&password=pass123&cc=123456789&email=john@example.com"
```

Login example (basic auth):

```bash
curl -X POST -u "john@example.com:pass123" "http://localhost:8080/login"
```

It returns JWT token string.

### 3) Use protected routes

Send JWT in `Authorization: Bearer <token>` header.

Example (user role):

```bash
curl -H "Authorization: Bearer <token>" "http://localhost:8080/role"
```

Example (admin create event):

```bash
curl -X POST -H "Authorization: Bearer <admin-token>" "http://localhost:8080/Evento?arg1=EVENTO&nome=Conference&data_inicio=2026-03-20&data_fim=2026-03-20&local=ISEC&horaInicio=09:00&horaFim=18:00"
```

## Database

The server uses SQLite database file in `dataBase/serverdatabase.db` (created automatically). It initializes tables and default admin if missing.

## Notes

- The server security setup uses Spring Security filter chains (unauthenticated for `/register`, `/swagger-ui/**`, `/v3/**`; HTTP Basic for `/login`; JWT for other endpoints).
- Keep JWT token expiry and code validity in mind when testing.
- Update `application.properties` if you need custom ports or DB paths.

## Project Evaluation Focus

This project demonstrates distributed programming competencies:
- client-server communication
- secure authentication and authorization
- persistence and database operations
- multi-role App logic (admin/client)
- REST API design for event and attendance workflows
