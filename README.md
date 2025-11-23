# Medicine Expiry Tracker

A full-stack application for tracking medicine batches, expiry dates, and storage locations.

## Tech Stack

### Frontend
- React 18.3.1
- React Router DOM
- Firebase Authentication
- Framer Motion (animations)
- Axios (API calls)

### Backend
- Spring Boot 3.2.0
- Java 21
- H2 Database (dev) / MySQL (production)
- JPA/Hibernate
- Lombok
- Swagger/OpenAPI

### Services
- OTP Email Verification (Node.js + Nodemailer)

## Project Structure

```
medicine-expiry-tracker/
├── frontend-react/          # React frontend application
├── backend-springboot/      # Spring Boot REST API
└── otp-service/            # Email OTP verification service
```

## Setup Instructions

### Prerequisites
- Node.js 18+ (for frontend & OTP service)
- Java 21 (auto-installed at `C:\Users\utkar\.jdk\jdk-21.0.8`)
- Maven (included via wrapper)

### 1. Start OTP Service

```bash
cd otp-service
npm install
npm start
```

Runs on: http://localhost:4000

### 2. Start Backend

**Windows:**
```bash
cd backend-springboot
start-backend.bat
```

**Manual:**
```bash
cd backend-springboot
set JAVA_HOME=C:\Users\utkar\.jdk\jdk-21.0.8
mvnw.cmd spring-boot:run -Dspring-boot.run.arguments=--spring.profiles.active=dev
```

Runs on: http://localhost:8080
- API: http://localhost:8080/api
- H2 Console: http://localhost:8080/h2-console
- Swagger UI: http://localhost:8080/swagger-ui.html

**H2 Console Login:**
- JDBC URL: `jdbc:h2:mem:medicine_tracker_dev`
- Username: `sa`
- Password: (leave blank)

### 3. Start Frontend

```bash
cd frontend-react
npm install
npm run dev
```

Runs on: http://localhost:5173

## Features

### Authentication
- ✅ Firebase email/password authentication
- ✅ Email OTP verification (5-digit code)
- ✅ Two-step signup flow
- ✅ Protected routes
- ✅ User profile management

### Medicine Management
- Medicine CRUD operations
- Batch tracking with expiry dates
- Storage slot management
- Real-time expiry notifications

### User Interface
- Modern, responsive design
- Animated loading states
- Form validation
- Error handling
- Dashboard with quick stats

## API Endpoints

### Medicines
- `GET /api/medicines` - List all medicines
- `POST /api/medicines` - Create medicine
- `GET /api/medicines/{id}` - Get medicine
- `PUT /api/medicines/{id}` - Update medicine
- `DELETE /api/medicines/{id}` - Delete medicine

### Batches
- `GET /api/batches` - List all batches
- `POST /api/batches` - Create batch
- `GET /api/batches/{id}` - Get batch
- `PUT /api/batches/{id}` - Update batch
- `DELETE /api/batches/{id}` - Delete batch
- `GET /api/batches/expiring-soon` - Get expiring batches

### Slots
- `GET /api/slots` - List all slots
- `POST /api/slots` - Create slot
- `GET /api/slots/{id}` - Get slot
- `PUT /api/slots/{id}` - Update slot
- `DELETE /api/slots/{id}` - Delete slot

## Environment Configuration

### OTP Service (.env)
```
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
SMTP_USER=your-email@gmail.com
SMTP_PASS=your-app-password
EMAIL_FROM=your-email@gmail.com
```

### Backend Profiles
- **dev**: H2 in-memory database (auto-created on startup)
- **prod**: MySQL database (requires setup)

## Development Notes

- Backend uses Java 21 (not compatible with Java 24)
- Frontend configured to call APIs at http://localhost:8080/api
- OTP codes expire after 5 minutes
- H2 database resets on each restart (dev profile)

## Troubleshooting

### Backend won't start
- Ensure Java 21 is installed: `java -version`
- Use the `start-backend.bat` script which sets correct JAVA_HOME
- Check port 8080 is not in use

### OTP emails not sending
- Verify Gmail app password in `otp-service/.env`
- Enable 2FA and generate app password in Gmail settings
- Check firewall/antivirus not blocking port 587

### Frontend can't connect to backend
- Ensure backend is running on port 8080
- Check browser console for CORS errors
- Verify `frontend-react/src/api/api.js` has correct base URL

## License

MIT
