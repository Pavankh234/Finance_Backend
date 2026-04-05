# Finance Data Processing and Access Control Backend

A comprehensive Spring Boot backend application for managing financial records with role-based access control, designed for a finance dashboard system.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [API Documentation](#api-documentation)
- [Authentication](#authentication)
- [Role-Based Access Control](#role-based-access-control)
- [Testing](#testing)
- [Design Patterns & Principles](#design-patterns--principles)
- [Assumptions & Trade-offs](#assumptions--trade-offs)

## Overview

This backend system provides APIs for:
- User authentication and authorization with JWT tokens
- User and role management
- Financial records (income/expense) management
- Dashboard analytics and summaries
- Role-based access control (RBAC)

## Features

### Core Features
- **User Management**: Create, update, delete, and search users
- **Role-Based Access Control**: Three roles (VIEWER, ANALYST, ADMIN) with granular permissions
- **Financial Records**: Full CRUD operations with filtering, pagination, and soft delete
- **Dashboard Analytics**: Summary statistics, category breakdowns, and monthly trends
- **JWT Authentication**: Secure token-based authentication

### Additional Features
- Input validation with detailed error messages
- Soft delete functionality for financial records
- Pagination and sorting for list endpoints
- Search functionality for users and records
- OpenAPI/Swagger documentation
- Comprehensive unit and integration tests

## Technology Stack

| Technology | Purpose |
|------------|---------|
| Java 21 | Programming language |
| Spring Boot 3.2 | Application framework |
| Spring Security | Authentication & Authorization |
| Spring Data JPA | Data persistence |
| MySQL 8.x | Relational database |
| JWT (jjwt) | Token-based authentication |
| SpringDoc OpenAPI | API documentation |
| JUnit 5 & Mockito | Testing |

## Architecture

The application follows a **layered architecture** with clear separation of concerns:

```
┌─────────────────────────────────────────────────────────┐
│                    Controller Layer                      │
│         (REST endpoints, request/response handling)      │
├─────────────────────────────────────────────────────────┤
│                     Service Layer                        │
│            (Business logic, validation)                  │
├─────────────────────────────────────────────────────────┤
│                   Repository Layer                       │
│              (Data access, JPA queries)                  │
├─────────────────────────────────────────────────────────┤
│                    Domain Layer                          │
│               (Entities, enums, DTOs)                    │
└─────────────────────────────────────────────────────────┘
```

### Project Structure

```
src/main/java/com/finance/
├── FinanceBackendApplication.java    # Main application entry point
├── config/                           # Configuration classes
│   ├── DataInitializer.java          # Database seeding
│   ├── OpenApiConfig.java            # Swagger configuration
│   └── SecurityConfig.java           # Security configuration
├── controller/                       # REST controllers
│   ├── AuthController.java
│   ├── DashboardController.java
│   ├── FinancialRecordController.java
│   └── UserController.java
├── domain/entity/                    # JPA entities
│   ├── FinancialRecord.java
│   ├── Role.java
│   └── User.java
├── dto/                              # Data Transfer Objects
│   ├── request/                      # Request DTOs
│   └── response/                     # Response DTOs
├── exception/                        # Custom exceptions & handlers
│   ├── AccessDeniedException.java
│   ├── BadRequestException.java
│   ├── DuplicateResourceException.java
│   ├── GlobalExceptionHandler.java
│   └── ResourceNotFoundException.java
├── repository/                       # JPA repositories
│   ├── FinancialRecordRepository.java
│   ├── RoleRepository.java
│   └── UserRepository.java
├── security/                         # Security components
│   ├── CustomUserDetailsService.java
│   ├── JwtAuthenticationEntryPoint.java
│   ├── JwtAuthenticationFilter.java
│   ├── JwtTokenProvider.java
│   └── SecurityUtils.java
└── service/                          # Business logic
    ├── AuthService.java
    ├── DashboardService.java
    ├── FinancialRecordService.java
    ├── UserService.java
    └── specification/
        └── FinancialRecordSpecification.java
```

## Getting Started

### Prerequisites

- Java 21 or higher
- Maven 3.6+
- MySQL 8.x

### Database Setup

1. **Install MySQL** (if not already installed)

2. **Create the database** (optional - app creates it automatically)
   ```sql
   CREATE DATABASE financedb;
   ```

3. **Configure database credentials** in `application.yml`:
   ```yaml
   spring:
     datasource:
       url: jdbc:mysql://localhost:3306/financedb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true
       username: root
       password: root
   ```

### Running the Application

1. **Clone the repository**
   ```bash
   cd finance-backend
   ```

2. **Start MySQL server**

3. **Build the project**
   ```bash
   mvn clean install -DskipTests
   ```

4. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

5. **Access the application**
   - API Base URL: `http://localhost:8080`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`

### Default Users

The application seeds three default users on startup:

| Username | Password | Role | Description |
|----------|----------|------|-------------|
| admin | admin123 | ADMIN | Full system access |
| analyst | analyst123 | ANALYST | Can view and create records |
| viewer | viewer123 | VIEWER | Read-only access |

## API Documentation

### Authentication Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/api/auth/login` | Login and get JWT token | Public |
| POST | `/api/auth/register` | Register new user (VIEWER role) | Public |

### User Management Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/users` | Get all users (paginated) | ADMIN, ANALYST |
| GET | `/api/users/{id}` | Get user by ID | ADMIN, ANALYST |
| GET | `/api/users/me` | Get current user profile | Authenticated |
| GET | `/api/users/search?q=` | Search users | ADMIN, ANALYST |
| POST | `/api/users` | Create new user | ADMIN |
| PUT | `/api/users/{id}` | Update user | ADMIN |
| PATCH | `/api/users/{id}/status` | Update user status | ADMIN |
| DELETE | `/api/users/{id}` | Delete user | ADMIN |

### Financial Records Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/records` | Get all records (paginated) | ALL |
| GET | `/api/records/{id}` | Get record by ID | ALL |
| GET | `/api/records/filter` | Filter records | ALL |
| GET | `/api/records/recent` | Get recent records | ALL |
| POST | `/api/records` | Create record | ANALYST, ADMIN |
| PUT | `/api/records/{id}` | Update record | ANALYST, ADMIN |
| DELETE | `/api/records/{id}` | Soft delete record | ADMIN |
| POST | `/api/records/{id}/restore` | Restore deleted record | ADMIN |
| DELETE | `/api/records/{id}/permanent` | Permanently delete | ADMIN |

### Dashboard Endpoints

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| GET | `/api/dashboard/summary` | Get full dashboard summary | ALL |
| GET | `/api/dashboard/summary/period` | Get summary for date range | ALL |
| GET | `/api/dashboard/totals/income` | Get total income | ALL |
| GET | `/api/dashboard/totals/expenses` | Get total expenses | ALL |
| GET | `/api/dashboard/totals/balance` | Get net balance | ALL |
| GET | `/api/dashboard/categories/income` | Income by category | ANALYST, ADMIN |
| GET | `/api/dashboard/categories/expenses` | Expenses by category | ANALYST, ADMIN |
| GET | `/api/dashboard/trends/monthly` | Monthly trends | ANALYST, ADMIN |

### Example API Calls

**Login:**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username": "admin", "password": "admin123"}'
```

**Create Financial Record:**
```bash
curl -X POST http://localhost:8080/api/records \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <your-jwt-token>" \
  -d '{
    "amount": 5000.00,
    "type": "INCOME",
    "category": "SALARY",
    "date": "2024-01-15",
    "description": "Monthly salary"
  }'
```

**Get Dashboard Summary:**
```bash
curl -X GET http://localhost:8080/api/dashboard/summary \
  -H "Authorization: Bearer <your-jwt-token>"
```

## Authentication

The application uses JWT (JSON Web Token) for authentication.

### Token Flow
1. User sends credentials to `/api/auth/login`
2. Server validates credentials and returns JWT token
3. Client includes token in `Authorization` header for subsequent requests
4. Token format: `Bearer <token>`

### Token Configuration
- Expiration: 24 hours (configurable in `application.yml`)
- Algorithm: HMAC-SHA256

## Role-Based Access Control

### Roles and Permissions

| Role | Permissions |
|------|-------------|
| **VIEWER** | View dashboard, View records |
| **ANALYST** | All VIEWER permissions + Create/Update records, View analytics, View users |
| **ADMIN** | All permissions including user management and record deletion |

### Permission Matrix

| Action | VIEWER | ANALYST | ADMIN |
|--------|--------|---------|-------|
| View Dashboard | ✅ | ✅ | ✅ |
| View Records | ✅ | ✅ | ✅ |
| Create Records | ❌ | ✅ | ✅ |
| Update Records | ❌ | ✅ | ✅ |
| Delete Records | ❌ | ❌ | ✅ |
| View Analytics | ❌ | ✅ | ✅ |
| View Users | ❌ | ✅ | ✅ |
| Manage Users | ❌ | ❌ | ✅ |

## Testing

### Running Tests

```bash
# Run all tests
mvn test

# Run with coverage report
mvn test jacoco:report
```

### Test Coverage

The project includes:
- **Unit Tests**: Service layer tests with Mockito
- **Integration Tests**: Controller tests with MockMvc
- **Security Tests**: Role-based access control verification

### Test Structure

```
src/test/java/com/finance/
├── FinanceBackendApplicationTests.java
├── controller/
│   ├── AuthControllerTest.java
│   ├── DashboardControllerTest.java
│   └── FinancialRecordControllerTest.java
└── service/
    ├── AuthServiceTest.java
    ├── DashboardServiceTest.java
    ├── FinancialRecordServiceTest.java
    └── UserServiceTest.java
```

## Design Patterns & Principles

### Design Patterns Used

1. **Repository Pattern**: Data access abstraction via Spring Data JPA repositories
2. **DTO Pattern**: Separate request/response objects from domain entities
3. **Builder Pattern**: Used extensively via Lombok `@Builder` for object creation
4. **Strategy Pattern**: JPA Specifications for dynamic query building
5. **Factory Pattern**: Response builders for consistent API responses
6. **Singleton Pattern**: Spring-managed beans (services, repositories)

### SOLID Principles

- **Single Responsibility**: Each class has one reason to change
- **Open/Closed**: Extensible via interfaces and specifications
- **Liskov Substitution**: Proper inheritance hierarchies
- **Interface Segregation**: Focused interfaces (repositories, services)
- **Dependency Inversion**: Constructor injection throughout

### Other Best Practices

- **Layered Architecture**: Clear separation between controllers, services, and repositories
- **Immutable DTOs**: Request/Response objects are immutable
- **Validation**: Input validation at controller level with Bean Validation
- **Exception Handling**: Centralized exception handling with `@RestControllerAdvice`
- **Logging**: Structured logging with SLF4J
- **Security**: Method-level security with `@PreAuthorize`

## Assumptions & Trade-offs

### Assumptions

1. **In-Memory Database**: H2 is used for simplicity; easily switchable to PostgreSQL/MySQL
2. **Single Instance**: No distributed caching or session management
3. **Simplified Categories**: Predefined enum-based categories for records
4. **Date Validation**: Records cannot have future dates
5. **Soft Delete**: Financial records use soft delete by default

### Trade-offs

| Decision | Benefit | Trade-off |
|----------|---------|-----------|
| H2 Database | Easy setup, no external dependencies | Data not persisted across restarts |
| JWT Authentication | Stateless, scalable | Token revocation requires additional infrastructure |
| Enum Categories | Type safety, validation | Less flexible than database-driven categories |
| Soft Delete | Data recovery possible | Requires filtering in all queries |
| Eager Role Loading | Simplified authorization | Potential N+1 queries |

### Future Improvements

- [ ] Add refresh token mechanism
- [ ] Implement rate limiting
- [ ] Add audit logging
- [ ] Support for file attachments on records
- [ ] Export functionality (CSV, PDF)
- [ ] Email notifications
- [ ] Multi-tenancy support

## Configuration

### Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SERVER_PORT` | Application port | 8080 |
| `APP_JWT_SECRET` | JWT signing key | (configured in yml) |
| `APP_JWT_EXPIRATION_MS` | Token expiration | 86400000 (24h) |

### MySQL Configuration

The application is configured to use MySQL by default. Update `application.yml` if needed:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/financedb?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true&createDatabaseIfNotExist=true
    username: your_username
    password: your_password
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQLDialect
```

## License

This project is created for assessment purposes.

---

**Author**: Finance Backend Team  
**Version**: 1.0.0
