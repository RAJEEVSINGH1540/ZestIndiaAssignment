# ZestIndia Assignment — Employee Management System

![Java](https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green?style=for-the-badge&logo=springboot)
![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?style=for-the-badge&logo=mysql)
![JWT](https://img.shields.io/badge/JWT-Authentication-red?style=for-the-badge&logo=jsonwebtokens)
![Maven](https://img.shields.io/badge/Maven-Build-purple?style=for-the-badge&logo=apachemaven)
![License](https://img.shields.io/badge/License-MIT-yellow?style=for-the-badge)

---

## 📋 Table of Contents

- [Overview](#-overview)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Database Design](#-database-design)
- [How To Run](#-how-to-run)
- [API Endpoints](#-api-endpoints)
- [Request & Response Examples](#-request--response-examples)
- [Pagination & Sorting](#-pagination--sorting)
- [Search Employees](#-search-employees)
- [HTTP Status Codes](#-http-status-codes)
- [How Authentication Works](#-how-authentication-works)
- [ID Generation Logic](#-id-generation-logic)
- [Postman Testing](#-postman-testing)
- [Running Unit Tests](#-running-unit-tests)
- [Security Configuration](#-security-configuration)

---

## 📌 Overview

A **Spring Boot REST API** application for managing employee records.

This application provides:
- ✅ User Registration and Login with **JWT Authentication**
- ✅ Secure password storage using **BCrypt hashing**
- ✅ Full **CRUD operations** for Employee records
- ✅ **Pagination and Sorting** for listing employees
- ✅ **Search** employees by name, department, and status
- ✅ **Role-based Authorization** — ADMIN and USER roles
- ✅ Custom **ID Generation** with year-based prefix
- ✅ **Unit Tests** for Service and Repository layers
- ✅ **Global Exception Handling** with consistent error responses

---

## 🛠 Tech Stack

| Technology        | Version   | Purpose                          |
|-------------------|-----------|----------------------------------|
| Java              | 17        | Programming Language             |
| Spring Boot       | 3.2.0     | Application Framework            |
| Spring Security   | 6.2       | Authentication & Authorization   |
| Spring Data JPA   | 3.2.0     | Database ORM Layer               |
| Hibernate         | 6.4       | JPA Implementation               |
| MySQL             | 8.0       | Relational Database              |
| JWT (jjwt)        | 0.11.5    | Token-based Authentication       |
| BCrypt            | Built-in  | Password Hashing (strength=12)   |
| Lombok            | Latest    | Boilerplate Code Reduction       |
| Maven             | 3.x       | Build & Dependency Management    |
| JUnit 5           | Latest    | Unit Testing Framework           |
| Mockito           | Latest    | Mocking Framework for Tests      |
| H2 Database       | Latest    | In-Memory DB for Tests           |

---

## 📁 Project Structure

```
ZestIndiaAssignment/
│
├── postman/
│   └── ZestIndia-Assignment.postman_collection.json
│
├── src/
│   ├── main/
│   │   ├── java/org/example/zestindiaassignment/
│   │   │   │
│   │   │   ├── ZestIndiaAssignmentApplication.java
│   │   │   │
│   │   │   ├── myprofilemodule/
│   │   │   │   └── util/
│   │   │   │       └── SimpleIdGenerator.java
│   │   │   │
│   │   │   ├── usermodule/
│   │   │   │   ├── controller/
│   │   │   │   │   └── AuthController.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── LoginRequestDto.java
│   │   │   │   │   ├── LoginResponseDto.java
│   │   │   │   │   └── UserRegistrationDto.java
│   │   │   │   ├── entity/
│   │   │   │   │   └── User.java
│   │   │   │   ├── repository/
│   │   │   │   │   └── UserRepository.java
│   │   │   │   ├── service/
│   │   │   │   │   ├── UserService.java
│   │   │   │   │   └── impl/
│   │   │   │   │       └── UserServiceImpl.java
│   │   │   │   └── utils/
│   │   │   │       └── IdGeneratorUtil.java
│   │   │   │
│   │   │   ├── employeemodule/
│   │   │   │   ├── controller/
│   │   │   │   │   └── EmployeeController.java
│   │   │   │   ├── dto/
│   │   │   │   │   ├── EmployeeRequestDto.java
│   │   │   │   │   └── EmployeeResponseDto.java
│   │   │   │   ├── entity/
│   │   │   │   │   └── Employee.java
│   │   │   │   ├── repository/
│   │   │   │   │   └── EmployeeRepository.java
│   │   │   │   └── service/
│   │   │   │       ├── EmployeeService.java
│   │   │   │       └── impl/
│   │   │   │           └── EmployeeServiceImpl.java
│   │   │   │
│   │   │   ├── security/
│   │   │   │   ├── CustomUserDetailsService.java
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   ├── JwtTokenProvider.java
│   │   │   │   └── SecurityConfig.java
│   │   │   │
│   │   │   └── common/
│   │   │       ├── exception/
│   │   │       │   └── GlobalExceptionHandler.java
│   │   │       └── response/
│   │   │           └── ApiResponse.java
│   │   │
│   │   └── resources/
│   │       └── application.properties
│   │
│   └── test/
│       ├── java/org/example/zestindiaassignment/
│       │   ├── employeemodule/
│       │   │   ├── repository/
│       │   │   │   └── EmployeeRepositoryTest.java
│       │   │   └── service/
│       │   │       └── EmployeeServiceTest.java
│       │   └── usermodule/
│       │       └── service/
│       │           └── UserServiceTest.java
│       └── resources/
│           └── application-test.properties
│
├── .gitignore
├── pom.xml
└── README.md
```

---

## 🗄 Database Design

### users table

| Column      | Type         | Constraints                    |
|-------------|--------------|--------------------------------|
| user_id     | VARCHAR(20)  | PRIMARY KEY — e.g. USR2025-0042 |
| username    | VARCHAR(50)  | NOT NULL, UNIQUE               |
| email       | VARCHAR(100) | NOT NULL, UNIQUE               |
| password    | VARCHAR(255) | NOT NULL — BCrypt hashed       |
| full_name   | VARCHAR(100) | NOT NULL                       |
| role        | ENUM         | ADMIN or USER                  |
| is_active   | BOOLEAN      | DEFAULT true                   |
| created_at  | DATETIME     | Auto-generated on insert       |
| updated_at  | DATETIME     | Auto-updated on every save     |

---

### employees table

| Column          | Type         | Constraints                      |
|-----------------|--------------|----------------------------------|
| emp_id          | VARCHAR(20)  | PRIMARY KEY — e.g. EMP2025-1234  |
| name            | VARCHAR(100) | NOT NULL                         |
| email           | VARCHAR(100) | NOT NULL, UNIQUE                 |
| department      | VARCHAR(100) | NOT NULL                         |
| position        | VARCHAR(100) | NOT NULL                         |
| salary          | DECIMAL      | NOT NULL, greater than 0         |
| date_of_joining | DATE         | NOT NULL, past or present        |
| status          | ENUM         | ACTIVE / INACTIVE / TERMINATED   |
| created_at      | DATETIME     | Auto-generated on insert         |
| updated_at      | DATETIME     | Auto-updated on every save       |

---

## 🚀 How To Run

### ✅ Prerequisites

Make sure the following are installed on your machine:

```
Java 17+
Maven 3.x
MySQL 8.0
```

---

### Step 1 — Clone the Repository

```bash
git clone https://github.com/YOUR_USERNAME/ZestIndiaAssignment.git
cd ZestIndiaAssignment
```

---

### Step 2 — Create MySQL Database

Open MySQL and run:

```sql
CREATE DATABASE zest_india_db;
```

---

### Step 3 — Configure Database Password

Open this file:

```
src/main/resources/application.properties
```

Update your MySQL credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/zest_india_db?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=mysql
```

---

### Step 4 — Build the Project

```bash
mvn clean install -DskipTests
```

---

### Step 5 — Run the Application

```bash
mvn spring-boot:run
```

---

### Step 6 — Verify Application Started

You should see in console:

```
Started ZestIndiaAssignmentApplication in X.XXX seconds
```

Application is running at:

```
http://localhost:8080
```

> Hibernate will **automatically create** the `users` and `employees`
> tables in MySQL on first startup.

---

## 🔗 API Endpoints

### Base URL

```
http://localhost:8080
```

---

### 🔓 Authentication Endpoints — Public (No Token Required)

| Method | Endpoint            | Description              |
|--------|---------------------|--------------------------|
| POST   | /api/auth/register  | Register a new user      |
| POST   | /api/auth/login     | Login and get JWT token  |

---

### 🔒 Employee Endpoints — Protected (JWT Token Required)

| Method | Endpoint                   | Role        | Description                        |
|--------|----------------------------|-------------|------------------------------------|
| POST   | /api/employees             | ADMIN only  | Create a new employee              |
| GET    | /api/employees             | ADMIN, USER | Get all employees (paginated)      |
| GET    | /api/employees/{empId}     | ADMIN, USER | Get single employee by ID          |
| GET    | /api/employees/search      | ADMIN, USER | Search employees with filters      |
| PUT    | /api/employees/{empId}     | ADMIN only  | Update an existing employee        |
| DELETE | /api/employees/{empId}     | ADMIN only  | Delete an employee                 |

> **How to send token:**
> Add this header to every protected request:
> ```
> Authorization: Bearer YOUR_JWT_TOKEN_HERE
> ```

---

## 📨 Request & Response Examples

### Register User

**Request:**
```http
POST /api/auth/register
Content-Type: application/json
```

```json
{
    "username": "adminuser",
    "email": "admin@zestindia.com",
    "password": "admin123",
    "fullName": "Admin User",
    "role": "ADMIN"
}
```

**Response — 201 Created:**
```json
{
    "success": true,
    "message": "User registered successfully",
    "data": {
        "userId": "USR2025-0042",
        "username": "adminuser",
        "email": "admin@zestindia.com",
        "fullName": "Admin User",
        "role": "ADMIN"
    },
    "timestamp": "2025-01-15T10:30:00"
}
```

---

### Login User

**Request:**
```http
POST /api/auth/login
Content-Type: application/json
```

```json
{
    "usernameOrEmail": "adminuser",
    "password": "admin123"
}
```

**Response — 200 OK:**
```json
{
    "success": true,
    "message": "Login successful",
    "data": {
        "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbnVzZXIi...",
        "tokenType": "Bearer",
        "userId": "USR2025-0042",
        "username": "adminuser",
        "email": "admin@zestindia.com",
        "role": "ADMIN",
        "expiresIn": 86400000
    },
    "timestamp": "2025-01-15T10:31:00"
}
```

---

### Create Employee

**Request:**
```http
POST /api/employees
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

```json
{
    "name": "Alice Johnson",
    "email": "alice@zestindia.com",
    "department": "Engineering",
    "position": "Senior Developer",
    "salary": 95000.00,
    "dateOfJoining": "2024-01-15",
    "status": "ACTIVE"
}
```

**Response — 201 Created:**
```json
{
    "success": true,
    "message": "Employee created",
    "data": {
        "empId": "EMP2025-1234",
        "name": "Alice Johnson",
        "email": "alice@zestindia.com",
        "department": "Engineering",
        "position": "Senior Developer",
        "salary": 95000.00,
        "dateOfJoining": "2024-01-15",
        "status": "ACTIVE",
        "createdAt": "2025-01-15T10:32:00",
        "updatedAt": "2025-01-15T10:32:00"
    },
    "timestamp": "2025-01-15T10:32:00"
}
```

---

### Get Employee By ID

**Request:**
```http
GET /api/employees/EMP2025-1234
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**Response — 200 OK:**
```json
{
    "success": true,
    "message": "Employee fetched successfully",
    "data": {
        "empId": "EMP2025-1234",
        "name": "Alice Johnson",
        "email": "alice@zestindia.com",
        "department": "Engineering",
        "position": "Senior Developer",
        "salary": 95000.00,
        "dateOfJoining": "2024-01-15",
        "status": "ACTIVE",
        "createdAt": "2025-01-15T10:32:00",
        "updatedAt": "2025-01-15T10:32:00"
    }
}
```

---

### Update Employee

**Request:**
```http
PUT /api/employees/EMP2025-1234
Content-Type: application/json
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

```json
{
    "name": "Alice Johnson",
    "email": "alice@zestindia.com",
    "department": "Engineering",
    "position": "Lead Developer",
    "salary": 110000.00,
    "dateOfJoining": "2024-01-15",
    "status": "ACTIVE"
}
```

**Response — 200 OK:**
```json
{
    "success": true,
    "message": "Employee updated successfully",
    "data": {
        "empId": "EMP2025-1234",
        "name": "Alice Johnson",
        "email": "alice@zestindia.com",
        "department": "Engineering",
        "position": "Lead Developer",
        "salary": 110000.00,
        "dateOfJoining": "2024-01-15",
        "status": "ACTIVE",
        "createdAt": "2025-01-15T10:32:00",
        "updatedAt": "2025-01-15T11:00:00"
    }
}
```

---

### Delete Employee

**Request:**
```http
DELETE /api/employees/EMP2025-1234
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

**Response — 200 OK:**
```json
{
    "success": true,
    "message": "Employee deleted successfully",
    "timestamp": "2025-01-15T11:05:00"
}
```

---

### Error Response Format

All errors follow this consistent format:

```json
{
    "success": false,
    "message": "Resource not found",
    "error": "Employee not found with ID: EMP2025-9999",
    "timestamp": "2025-01-15T11:06:00"
}
```

---

## 📄 Pagination & Sorting

Get all employees with pagination and sorting using query parameters:

```
GET /api/employees?page=0&size=5&sortBy=salary&sortDir=desc
```

| Parameter | Type    | Default | Description                                 |
|-----------|---------|---------|---------------------------------------------|
| page      | Integer | 0       | Page number — starts from 0                 |
| size      | Integer | 10      | Number of records per page                  |
| sortBy    | String  | empId   | Field to sort — name, salary, department    |
| sortDir   | String  | asc     | Sort direction — asc or desc                |

**Paginated Response:**
```json
{
    "success": true,
    "message": "Employees fetched successfully",
    "data": {
        "content": [
            {
                "empId": "EMP2025-1234",
                "name": "Alice Johnson",
                "salary": 110000.00,
                "..."  : "..."
            }
        ],
        "totalElements": 5,
        "totalPages": 1,
        "number": 0,
        "size": 10,
        "first": true,
        "last": true,
        "empty": false
    }
}
```

---

## 🔍 Search Employees

Search with optional filters combined with pagination and sorting:

```
GET /api/employees/search?department=Engineering&status=ACTIVE&sortBy=salary&sortDir=desc
```

| Parameter  | Type   | Required | Description                              |
|------------|--------|----------|------------------------------------------|
| name       | String | No       | Partial name search — case insensitive   |
| department | String | No       | Exact department match                   |
| status     | String | No       | ACTIVE / INACTIVE / TERMINATED           |
| page       | Integer| No       | Page number — default 0                  |
| size       | Integer| No       | Page size — default 10                   |
| sortBy     | String | No       | Field to sort — default name             |
| sortDir    | String | No       | asc or desc — default asc               |

**Example Searches:**

```bash
# Search by name
GET /api/employees/search?name=Alice

# Search by department
GET /api/employees/search?department=Engineering

# Search by status
GET /api/employees/search?status=ACTIVE

# Multiple filters
GET /api/employees/search?department=HR&status=ACTIVE

# All filters with pagination
GET /api/employees/search?name=Bob&department=HR&status=ACTIVE&page=0&size=5&sortBy=salary&sortDir=asc
```

---

## 📊 HTTP Status Codes

| Code | Status                | When It Happens                            |
|------|-----------------------|--------------------------------------------|
| 200  | OK                    | Successful GET, PUT, DELETE request        |
| 201  | Created               | Successful POST — register or create       |
| 400  | Bad Request           | Validation errors — missing or invalid fields |
| 401  | Unauthorized          | No token provided or wrong password        |
| 403  | Forbidden             | USER role tries to do ADMIN operation      |
| 404  | Not Found             | Employee or User ID does not exist         |
| 409  | Conflict              | Duplicate username or email                |
| 500  | Internal Server Error | Unexpected server error                    |

---

## 🔐 How Authentication Works

```
┌─────────────────────────────────────────────────────────┐
│                  STEP 1 — REGISTER                       │
│                                                          │
│  POST /api/auth/register                                 │
│  { username, email, password, fullName, role }           │
│                 │                                        │
│                 ▼                                        │
│  BCrypt hashes password (strength 12)                    │
│  Generate unique ID  →  USR2025-0042                     │
│  Save to MySQL users table                               │
│  Return userId, username, email, role                    │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│                  STEP 2 — LOGIN                          │
│                                                          │
│  POST /api/auth/login                                    │
│  { usernameOrEmail, password }                           │
│                 │                                        │
│                 ▼                                        │
│  Find user in DB by username OR email                    │
│  BCrypt.matches(rawPassword, hashedPassword)             │
│  ✓ Match   →  Generate JWT Token (24 hour expiry)        │
│  ✗ No Match → 401 Unauthorized                          │
│  Return token to client                                  │
└─────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────┐
│           STEP 3 — EVERY PROTECTED REQUEST               │
│                                                          │
│  Authorization: Bearer eyJhbGci...                       │
│                 │                                        │
│                 ▼                                        │
│  JwtAuthenticationFilter intercepts                      │
│  Validates token signature and expiry                    │
│  Extracts username from token                            │
│  Loads user from DB                                      │
│  Sets authentication in SecurityContext                  │
│                 │                                        │
│                 ▼                                        │
│  @PreAuthorize("hasRole('ADMIN')")                       │
│  ADMIN role → Allow ✓                                   │
│  USER role  → 403 Forbidden ✗                           │
└─────────────────────────────────────────────────────────┘
```

---

## 🔑 ID Generation Logic

Custom unique ID generation using `IdGeneratorUtil` + `SimpleIdGenerator`:

```
Format  :  PREFIX + CURRENT_YEAR + "-" + RANDOM_4_DIGITS

Examples:
  User ID     →  USR2025-0042
  Employee ID →  EMP2025-1234

Algorithm:
  1. Get current year  →  2025
  2. Build prefix      →  "EMP2025-"
  3. Generate random number between 1000 and 9999
  4. Build candidate ID  →  "EMP2025-1234"
  5. Check DB:
     SELECT 1 FROM employees WHERE emp_id = ? LIMIT 1
  6. Already exists? → Go back to step 3
  7. Does not exist? → Use this ID ✓
  8. Max 100 attempts before throwing exception
```

---

## 📮 Postman Testing

### Import Collection

```
1. Open Postman
2. Click "Import" button (top left)
3. Choose file:
   postman/ZestIndia-Assignment.postman_collection.json
4. Click Import
5. Collection appears in left sidebar
```

---

### Collection Variables

These variables are auto-managed by test scripts:

| Variable   | Set By Request       | Used In                          |
|------------|----------------------|----------------------------------|
| baseUrl    | Pre-set (default)    | All requests                     |
| adminToken | 05 - Login as ADMIN  | All protected ADMIN requests     |
| userToken  | 06 - Login as USER   | USER role test requests          |
| empId      | 09 - Create Employee | GET by ID, PUT, DELETE requests  |

---

### Recommended Run Order

```
── AUTH MODULE ──────────────────────────────────
01  Register ADMIN User
02  Register Normal USER
03  Register Duplicate Username  (expect 409)
04  Register Validation Error    (expect 400)
05  Login as ADMIN               ← saves adminToken
06  Login as Normal USER         ← saves userToken
07  Login Wrong Password         (expect 401)
08  Login by Email

── EMPLOYEE MODULE — WRITE ──────────────────────
09  Create Employee 1 Engineering  ← saves empId
10  Create Employee 2 HR
11  Create Employee 3 Finance
12  Create Employee 4 Engineering
13  Create Employee 5 Marketing INACTIVE
14  Create Duplicate Email        (expect 409)
15  Create No Token               (expect 401)
16  Create USER Token             (expect 403)
17  Update Employee               (ADMIN)
18  Update USER Token             (expect 403)
19  Update Not Found              (expect 404)
20  Delete Employee               (ADMIN)
21  Delete USER Token             (expect 403)

── EMPLOYEE MODULE — READ ───────────────────────
22  Get By ID — ADMIN Token
23  Get By ID — USER Token
24  Get By ID — Not Found         (expect 404)
25  Get All — Default Pagination
26  Get All — Page 0 Size 2
27  Get All — Sort by Name ASC
28  Get All — Sort by Salary DESC
29  Get All — Page 1 Size 2
30  Search by Name
31  Search by Department
32  Search by Status ACTIVE
33  Search by Status INACTIVE
34  Search by Department AND Status
35  Search by Name AND Department AND Status
36  Search with Pagination and Sorting
37  Search No Match               (expect empty)
38  Get All — USER Token          (should work)
```

---

## 🧪 Running Unit Tests

```bash
# Run all tests
mvn test

# Run only Employee Service tests
mvn test -Dtest=EmployeeServiceTest

# Run only Employee Repository tests
mvn test -Dtest=EmployeeRepositoryTest

# Run only User Service tests
mvn test -Dtest=UserServiceTest

# Run specific test method
mvn test -Dtest=EmployeeServiceTest#getById_Success

# Skip tests during build
mvn clean install -DskipTests
```

---

### Test Classes Overview

| Test Class               | Type              | What It Tests                              |
|--------------------------|-------------------|--------------------------------------------|
| EmployeeServiceTest      | Unit — Mockito    | create, read, update, delete, search logic |
| EmployeeRepositoryTest   | Integration — H2  | JPA queries, pagination, search filters    |
| UserServiceTest          | Unit — Mockito    | register validation, login, token          |

> Tests use **H2 in-memory database** — no MySQL needed for testing.

---

## 🛡 Security Configuration

| Setting           | Value                     |
|-------------------|---------------------------|
| Password Hashing  | BCrypt — strength 12       |
| Token Algorithm   | HMAC-SHA256 (HS256)        |
| Token Expiry      | 24 hours (86400000 ms)     |
| Session Policy    | STATELESS — no sessions    |
| CSRF              | Disabled — REST API        |
| Public Routes     | /api/auth/**               |
| Protected Routes  | Everything else            |

---

## 👤 Roles & Permissions

| Endpoint                   | PUBLIC | USER | ADMIN |
|----------------------------|--------|------|-------|
| POST /api/auth/register    | ✅     | ✅   | ✅    |
| POST /api/auth/login       | ✅     | ✅   | ✅    |
| POST /api/employees        | ❌     | ❌   | ✅    |
| GET  /api/employees        | ❌     | ✅   | ✅    |
| GET  /api/employees/{id}   | ❌     | ✅   | ✅    |
| GET  /api/employees/search | ❌     | ✅   | ✅    |
| PUT  /api/employees/{id}   | ❌     | ❌   | ✅    |
| DELETE /api/employees/{id} | ❌     | ❌   | ✅    |

---

## 👨‍💻 Author

```
Name        :  Your Name
Assignment  :  Java Spring Boot Hiring Assignment
Company     :  Zest India
Tech Stack  :  Java 17 + Spring Boot 3.2.0 + MySQL + JWT
GitHub      :  https://github.com/YOUR_USERNAME/ZestIndiaAssignment
```

---

## 📝 License

This project is built for assignment purposes for **Zest India**.
