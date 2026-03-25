# Airbnb Backend Setup Guide

## Prerequisites

- **Java 21**: Download from [oracle.com](https://www.oracle.com/java/technologies/downloads/#java21)
- **PostgreSQL 16** (or latest): Download from [postgresql.org](https://www.postgresql.org/download/windows/)
- **Maven** (included via mvnw script)

## Quick Start with PostgreSQL

### 1. Install PostgreSQL

Download and install PostgreSQL from the link above. During installation:

- **Database Superuser Password**: `password` (or change as needed)
- **Port**: `5432` (default)

After installation, create the database:

```powershell
# Open PostgreSQL command line
psql -U postgres

# Create the database
CREATE DATABASE airbnb_db;

# Exit
\q
```

### 2. Build the Project

```powershell
cd "c:\Users\subod\OneDrive\Desktop\Skills\Web Dev Pro\project clone\Airbnb\AirBnb"
.\mvnw clean install
```

### 3. Run the Application

**Option A: Using Maven** (Development profile with dev properties)

```powershell
# Set environment or use dev profile
$env:SPRING_PROFILES_ACTIVE = "dev"
.\mvnw spring-boot:run
```

**Option B: Using Java directly** (Production profile with environment variables)

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/airbnb_db"
$env:DB_USERNAME = "postgres"
$env:DB_PASSWORD = "password"
$env:JWT_SECRET = "your-secret-key"
$env:FRONTEND_URL = "http://localhost:3000"
$env:STRIPE_SECRET_KEY = "sk_test_xxxx"
$env:STRIPE_WEBHOOK_SECRET = "whsec_xxxx"

java -jar target/AirBnb-0.0.1-SNAPSHOT.jar
```

### 4. Access the Application

Once started successfully, you'll see:

```
  ╭━╭━╭━╭━╭━╭━╭━╭━╭━╭━╭━╭━╭━╭━╭━╭━╮
  ┃ Tomcat started on port(s): 8080
  ┃ Application started successfully
  ╰━╰━╰━╰━╰━╰━╰━╰━╰━╰━╰━╰━╰━╰━╰━╰━╯
```

**API Endpoints:**

- Base URL: http://localhost:8080/api/v1
- Swagger UI: http://localhost:8080/api/v1/swagger-ui.html
- H2 Database (if using H2): http://localhost:8080/h2-console

## Using Docker

### Option: Docker with Docker Compose

```powershell
# Build and run with Docker
docker-compose up

# The app will be available at http://localhost:8080/api/v1
```

This will:

- Pull and start a PostgreSQL container
- Build the application image
- Start the application connected to PostgreSQL

## Environment Variables Reference

| Variable                | Default                                      | Description           |
| ----------------------- | -------------------------------------------- | --------------------- |
| `DB_URL`                | `jdbc:postgresql://localhost:5432/airbnb_db` | Database JDBC URL     |
| `DB_USERNAME`           | `postgres`                                   | Database username     |
| `DB_PASSWORD`           | `password`                                   | Database password     |
| `JWT_SECRET`            | Required                                     | JWT signing secret    |
| `FRONTEND_URL`          | `http://localhost:3000`                      | Frontend URL for CORS |
| `STRIPE_SECRET_KEY`     | `sk_test_placeholder`                        | Stripe API key        |
| `STRIPE_WEBHOOK_SECRET` | `whsec_test`                                 | Stripe webhook secret |
| `SERVER_PORT`           | `8080`                                       | Application port      |

## Troubleshooting

### Port 8080 Already in Use

```powershell
# Find the process using port 8080
Get-NetTCPConnection -LocalPort 8080

# Kill the Java process (replace [PID] with the process ID)
Stop-Process -Id [PID] -Force
```

### PostgreSQL Connection Error

Ensure PostgreSQL is running and accessible:

```powershell
# Test connection
psql -h localhost -U postgres -d airbnb_db

# If password fails, check PostgreSQL is installed and running:
# Windows Services: Services.msc -> PostgreSQL Server
```

### Build Failures with Java Version

The project requires Java 21. Verify your version:

```powershell
java -version

# If using a different Java version, set JAVA_HOME:
$env:JAVA_HOME = "C:\Program Files\Java\jdk-21"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"
```

## Project Structure

```
AirBnb/
├── src/main/java/com/aman/AirBnb/AirBnb/
│   ├── Controller/          # REST endpoints
│   ├── Service/             # Business logic
│   ├── Repository/          # Database access
│   ├── Entities/            # JPA entities
│   ├── Dto/                 # Data transfer objects
│   ├── Security/            # JWT & Spring Security
│   ├── Config/              # Spring configuration
│   └── Advice/              # Global exception handling
└── src/main/resources/
    ├── application.properties        # Default config
    └── application-dev.properties    # Dev config
```

## API Features

- ✅ User authentication & JWT
- ✅ Hotel management & room inventory
- ✅ Booking system with real-time status
- ✅ Payment processing with Stripe
- ✅ Guest management
- ✅ Hotel search & filtering
- ✅ Booking history & reports
- ✅ Full Swagger documentation

---

**For detailed API documentation**: Visit `/api/v1/swagger-ui.html` after starting the application.
