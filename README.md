# Employee Profile Management System

A single-page HR application supporting role-based data access for employee profiles, feedback collection, and absence requests.

## 🏗️ Architecture

This project follows a modern full-stack architecture:

- **Frontend**: Angular 20+ with TypeScript
- **Backend**: Spring Boot 3.5+ with Java 21
- **Database**: Supabase (PostgreSQL)
- **Build Tools**: Maven (Backend), Angular CLI (Frontend)

## 📁 Project Structure

```
employee-profile/
├── backend/          # Spring Boot REST API
│   ├── src/
│   ├── pom.xml
│   └── .env         # Database configuration (not committed)
├── frontend/        # Angular application
│   ├── src/
│   ├── package.json
│   └── angular.json
└── README.md
```

## 🚀 Getting Started

### Prerequisites

- **Java 21** or higher
- **Node.js 18+** and npm
- **Maven 3.6+**
- **Supabase account** (for database)

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Create a `.env` file with your Supabase credentials:
   ```env
   SUPABASE_DB_HOST=db.your-project-ref.supabase.co
   SUPABASE_DB_PORT=5432
   SUPABASE_DB_NAME=postgres
   SUPABASE_DB_USER=postgres
   SUPABASE_DB_PASSWORD=your-database-password
   ```

3. Install dependencies and run:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

The backend will start on `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:
   ```bash
   cd frontend
   ```

2. Install dependencies:
   ```bash
   npm install
   ```

3. Start the development server:
   ```bash
   ng serve
   ```

The frontend will start on `http://localhost:4200`

## 🔧 API Endpoints

The backend provides the following REST endpoints:

- `GET /api/employees` - Get all employees
- `POST /api/employees` - Create new employee
- `PUT /api/employees/{id}` - Update employee
- `DELETE /api/employees/{id}` - Delete employee
- `GET /api/employees/search?name={name}` - Search by name
- `GET /api/employees/search?department={dept}` - Search by department

## 🗄️ Database

This project uses Supabase (PostgreSQL) for data persistence. The database schema is automatically managed by Hibernate with `ddl-auto=update`.

### Key Features:
- Automatic schema generation
- Connection pooling with HikariCP
- Environment-based configuration
- Sample data initialization

## 🔐 Security

- Environment variables for sensitive data
- CORS configuration for frontend integration
- Input validation with Bean Validation
- Secure credential management

## 🛠️ Development

### Running Tests

Backend:
```bash
cd backend
mvn test
```

Frontend:
```bash
cd frontend
ng test
```

### Building for Production

Backend:
```bash
cd backend
mvn clean package
```

Frontend:
```bash
cd frontend
ng build --configuration production
```
