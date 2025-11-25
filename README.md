# MediTrack - Pharmaceutical Inventory Management System

Enterprise-grade solution for pharmaceutical batch monitoring and expiry management with intelligent alerting.

## Technology Ecosystem

### Client Layer
- React 18.2.0
- React Router DOM 6.20.0
- Firebase 12.6.0
- Framer Motion 12.23.24
- Axios 1.6.2
- React Scripts 5.0.1

### Server Layer
- Spring Boot 3.2.0
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring DevTools
- Java 21
- H2 Database / MySQL
- Lombok 1.18.34
- SpringDoc OpenAPI 2.2.0
- Maven

### Authentication Service
- Node.js
- Express 5.1.0
- Nodemailer 7.0.10
- CORS 2.8.5
- dotenv 17.2.3

### Standalone JDBC Module
- Java 21
- JDBC API
- DAO Architecture
- Console Interface
- CSV Export Engine
- Automated Monitoring

## System Architecture

### JDBC Implementation Layer

Standalone Java module located in `src/app/` demonstrating enterprise JDBC patterns:

**Core Modules:**
- JDBC Connectivity via `java.sql` package
- Data Access Objects: `BatchDao`, `MedicineDao`, `SlotDao`
- PreparedStatement implementation
- ResultSet object mapping
- Transaction coordination
- Connection pooling in `DbUtil.java`
- Business services: `MedicineService`, `AlertService`
- Automated scheduler: `ExpiryScheduler`
- Report generation: `CsvExporter`

**JDBC Capabilities:**
- DriverManager connection handling
- CRUD implementations via PreparedStatement
- Bulk insert processing
- Transaction lifecycle management
- Metadata extraction
- Resource management

**Execution:**
```bash
cd src
javac -d ../target app/*.java dao/*.java model/*.java service/*.java util/*.java
java -cp ../target app.App
```

Spring Boot module (`backend-springboot/`) leverages JPA/Hibernate ORM while JDBC module provides lightweight data access.

## Directory Organization

```
medicine-expiry-tracker/
├── frontend-react/
│   ├── src/
│   │   ├── components/
│   │   ├── pages/
│   │   ├── context/
│   │   ├── hooks/
│   │   ├── api.js
│   │   └── firebaseConfig.js
│   └── build/
├── backend-springboot/
│   ├── src/main/java/com/medtracker/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── entity/
│   │   ├── dto/
│   │   ├── mapper/
│   │   ├── exception/
│   │   ├── config/
│   │   └── scheduler/
│   └── src/main/resources/
│       ├── application.properties
│       ├── application-dev.properties
│       ├── schema.sql
│       └── data.sql
├── otp-service/
│   ├── index.js
│   └── .env
├── src/
│   ├── app/
│   ├── dao/
│   ├── model/
│   ├── service/
│   └── util/
└── sql/
    ├── create_tables.sql
    └── sample_*.sql
```

## Installation & Deployment

### System Requirements
- Node.js 18+
- Java 21
- Maven

### OTP Service Initialization

```bash
cd otp-service
npm install
npm start
```

Service Port: http://localhost:4000

### Backend Initialization

**Windows Launch:**
```bash
cd backend-springboot
start-backend.bat
```

**Manual Launch:**
```bash
cd backend-springboot
set JAVA_HOME=C:\Users\utkar\.jdk\jdk-21.0.8
mvnw.cmd spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=dev
```

Service Port: http://localhost:8080

Endpoints:
- API: http://localhost:8080/api
- H2 Console: http://localhost:8080/h2-console
- Swagger: http://localhost:8080/swagger-ui.html

**H2 Access Credentials:**
- JDBC URL: `jdbc:h2:mem:medicine_tracker_dev`
- Username: `sa`
- Password: blank

### Frontend Initialization

```bash
cd frontend-react
npm install
npm start
```

Application URL: http://localhost:3000

## Functional Capabilities

### Authentication System
- Firebase authentication integration
- OTP-based email verification
- 5-digit verification codes
- 5-minute code validity
- Multi-step registration workflow
- Route protection mechanisms
- User account management
- Password recovery
- Session management

### Inventory Operations
- Medicine catalog management
- Batch lifecycle tracking
- Expiry date monitoring
- Storage allocation system
- Name-based search
- Medicine-batch relationships
- Batch identification lookup
- Location-based slot queries

### Monitoring Infrastructure
- Daily automated scans at 9:00 AM
- Weekly summary reports on Mondays at 10:00 AM
- Real-time expiry alerts with 30-day threshold
- Flexible date range queries
- Statistical dashboard

### Interface Design
- Responsive layout
- Motion-based transitions
- Input validation
- Progress indicators
- Alert notifications
- Tabbed navigation
- Interactive visualizations

### Data Operations
- CSV report generation
- Sample dataset provisioning
- Database persistence
- REST architecture

## REST API Specification

Documentation Portal: http://localhost:8080/swagger-ui.html

### Medicine Endpoints (`/api/medicines`)
- `GET /api/medicines`
- `GET /api/medicines/{id}`
- `GET /api/medicines/name/{name}`
- `POST /api/medicines`
- `PUT /api/medicines/{id}`
- `DELETE /api/medicines/{id}`

### Batch Endpoints (`/api/batches`)
- `GET /api/batches`
- `GET /api/batches/{id}`
- `GET /api/batches/number/{batchNumber}`
- `GET /api/batches/medicine/{medicineId}`
- `GET /api/batches/expiring?days=30`
- `GET /api/batches/expiring-between?start=YYYY-MM-DD&end=YYYY-MM-DD`
- `POST /api/batches`
- `PUT /api/batches/{id}`
- `DELETE /api/batches/{id}`

### Slot Endpoints (`/api/slots`)
- `GET /api/slots`
- `GET /api/slots/{id}`
- `GET /api/slots/number/{slotNumber}`
- `GET /api/slots/available`
- `GET /api/slots/location/{location}`
- `POST /api/slots`
- `PUT /api/slots/{id}`
- `DELETE /api/slots/{id}`

### Authentication Endpoints (`http://localhost:4000`)
- `POST /send-otp`
- `POST /verify-otp`

## Configuration Management

### OTP Service Settings
```
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USER=your-email@gmail.com
SMTP_PASS=your-app-password
EMAIL_FROM=your-email@gmail.com
```

### Environment Profiles
- dev: H2 in-memory database with auto-initialization
- prod: MySQL persistent storage

## Technical Specifications

### Backend Configuration
- Java Version: 21
- Build System: Maven with wrapper
- Profile Activation: `-Dspring-boot.run.arguments=--spring.profiles.active=dev`
- Database: H2 in-memory with automatic schema creation
- Sample Data: Auto-loaded in development mode
- Scheduled Operations:
  - Expiry verification: Daily at 9:00 AM
  - Summary generation: Weekly on Mondays at 10:00 AM

### Frontend Configuration
- Build System: Create React App
- API Endpoint: `http://localhost:8080/api`
- OTP Endpoint: `http://localhost:4000`
- Environment: `.env` configuration file
- Production Output: `build/` directory

### OTP Configuration
- Code Validity: 5 minutes
- SMTP Provider: Gmail
- Service Port: 4000

### Database Configuration
- Development: H2 in-memory `jdbc:h2:mem:medicine_tracker_dev`
- Production: MySQL `jdbc:mysql://localhost:3306/medicine_tracker`
- Schema Management: create-drop (dev), update (prod)

## Production Deployment

### Frontend Compilation
```bash
cd frontend-react
npm run build
```
Output: `build/` directory

### Backend Compilation
```bash
cd backend-springboot
mvnw.cmd clean package
```
Artifact: `target/medicine-expiry-tracker-1.0.0.jar`

**Deployment Execution:**
```bash
java -jar target/medicine-expiry-tracker-1.0.0.jar --spring.profiles.active=prod
```

## Quality Assurance

### Frontend Testing
```bash
cd frontend-react
npm test
```

### Backend Testing
```bash
cd backend-springboot
mvnw.cmd test
```

## Rapid Launch Sequence

**Terminal Session 1:**
```bash
cd otp-service && npm start
```

**Terminal Session 2:**
```bash
cd backend-springboot && start-backend.bat
```

**Terminal Session 3:**
```bash
cd frontend-react && npm start
```

Access Point: http://localhost:3000

## Problem Resolution

### Backend Diagnostics

**Launch Failures:**
- Verify Java installation: `java -version`
- Execute `start-backend.bat` for automatic configuration
- Check port availability: `netstat -ano | findstr :8080`
- Confirm Maven wrapper permissions

**Database Connectivity:**
- H2 verification: http://localhost:8080/h2-console
- MySQL verification: Confirm service status and credentials
- Configuration check: Review `application.properties` or `application-dev.properties`

**Compilation Issues:**
- Maven cache reset: `mvnw.cmd clean`
- Dependency refresh: `mvnw.cmd dependency:purge-local-repository`

### Frontend Diagnostics

**Backend Communication:**
- Confirm backend availability: http://localhost:8080/api
- Browser console CORS inspection
- API configuration verification in `frontend-react/src/api.js`
- Browser cache clearance

**Build Failures:**
- Remove `node_modules` and `package-lock.json`, execute `npm install`
- NPM cache purge: `npm cache clean --force`

**Firebase Integration:**
- Validate credentials in `firebaseConfig.js`
- Firebase console project verification
- Browser console error analysis

### OTP Service Diagnostics

**Email Delivery:**
- Confirm Gmail app password in `otp-service/.env`
- Enable two-factor authentication and generate app password
- Firewall/antivirus port 587 verification
- SMTP connectivity test: `telnet smtp.gmail.com 587`
- Service log inspection

**Port Conflicts:**
- Port utilization check: `netstat -ano | findstr :4000`
- Port modification in `otp-service/index.js`

### General Diagnostics

**Port Conflict Resolution:**
```powershell
netstat -ano | findstr :<port>
taskkill /PID <PID> /F
```

**Service Verification:**
- Backend: http://localhost:8080/api
- H2 Console: http://localhost:8080/h2-console
- API Documentation: http://localhost:8080/swagger-ui.html
- Frontend: http://localhost:3000
- OTP: http://localhost:4000

## Licensing

MIT
