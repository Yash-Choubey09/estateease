# EstateEase OPMS

An independently built educational demonstration of an Online Property Management System. It is inspired by the feature scope in the internship report, but it is **not the company's confidential project, source code, UI, or deployment**.

## Stack
- Java 17, Spring Boot 3.3.5, Maven
- Spring MVC + Thymeleaf (server-rendered UI)
- Spring Security form login, BCrypt password hashing, role-based URL access, CSRF protection
- Spring Data JPA / Hibernate + MySQL
- Bean Validation, HTML/CSS/JavaScript

## Implemented modules
1. Registration, login, logout; OWNER and TENANT roles.
2. Owner property CRUD with ownership checks.
3. Public property discovery and optional location, type, availability, and maximum-price filters.
4. Tenant inquiries; owner inbox and tenant inquiry history.

The database contains three main entities/tables: `app_users`, `properties`, and `inquiries`. Hibernate creates/updates the schema on first run (`ddl-auto=update`). Demo data is inserted only when the user table is empty.

## Requirements
- JDK 17+
- Maven 3.8+
- MySQL 8+
- Browser

## Run locally (Windows)
1. Install and start MySQL Server.
2. Open MySQL Workbench or terminal and run:
   ```sql
   CREATE DATABASE estateease_db;
   ```
3. In `src/main/resources/application.properties`, set the datasource username/password, or set environment variables `DB_USERNAME` and `DB_PASSWORD`.
4. Open a terminal in this project directory and run:
   ```bash
   mvn clean spring-boot:run
   ```
5. Visit http://localhost:8080

If using PowerShell environment variables for this terminal session:
```powershell
$env:DB_USERNAME="root"
$env:DB_PASSWORD="your_mysql_password"
mvn clean spring-boot:run
```

## Demo accounts
| Role | Email | Password |
|---|---|---|
| Owner | owner@estateease.demo | Owner@123 |
| Owner | meera@estateease.demo | Owner@123 |
| Tenant | tenant@estateease.demo | Tenant@123 |
| Tenant | kabir@estateease.demo | Tenant@123 |

The demo accounts are seeded on a fresh database. Registering a new account is also supported. Passwords are stored as BCrypt hashes.

## Suggested live demo sequence
1. Open Explore homes without signing in; try location and price filters.
2. Sign in as `owner@estateease.demo` and add a new listing.
3. Edit the listing and change availability; delete a test listing if desired.
4. Sign out and sign in as `tenant@estateease.demo`.
5. Find an AVAILABLE property, open details, and send an inquiry.
6. Sign out and return as the owner; open Inquiries and show the received message.
7. Sign in as tenant and demonstrate that `/owner/dashboard` is restricted.

## Important notes / limitations
- This is a classroom demo with seeded fictional data. Do not enter real personal or financial information.
- Demo passwords are intentionally simple and must never be used in production.
- No payments, lease/rent tracking, maintenance workflows, email/SMS notifications, image uploads, admin role, password reset, or production deployment hardening are implemented.
- The property images are loaded from Unsplash URLs and therefore require internet access; the application logic itself runs locally.
- For a clean reseed, stop the app, drop and recreate `estateease_db`, then restart.

## Project structure
```text
src/main/java/com/estateease
  config/       Security configuration and demo data seeder
  controller/   MVC controllers
  model/        AppUser, Property, Inquiry, enums
  repository/   Spring Data JPA repositories
  security/     Custom UserDetailsService
  service/      Current user helper
src/main/resources
  templates/    Thymeleaf pages
  static/css/   Responsive styling
  application.properties
```
