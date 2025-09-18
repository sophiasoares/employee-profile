# Employee Profile Management System

A single-page HR application supporting role-based data access for employee profiles, feedback collection, and absence requests.

## 🏗️ Architecture

This project follows a modern full-stack architecture:

- **Frontend**: Angular 20+ with TypeScript
- **Backend**: Spring Boot 3.5+ with Java 17
- **Database**: Supabase (PostgreSQL)
- **Build Tools**: Maven (Backend), Angular CLI (Frontend)

## 📁 Project Structure

```
employee-profile/
├── backend/                    # Spring Boot REST API
│   ├── src/main/java/com/employeeprofile/backend/
│   │   ├── entity/            # JPA Entities
│   │   │   ├── Employee.java
│   │   │   ├── Feedback.java
│   │   │   ├── AbsenceRequest.java
│   │   │   ├── EmploymentType.java
│   │   │   ├── EmploymentStatus.java
│   │   │   ├── AbsenceType.java
│   │   │   ├── AbsenceStatus.java
│   │   │   ├── HalfDayPeriod.java
│   │   │   ├── FeedbackType.java
│   │   │   └── FeedbackStatus.java
│   │   ├── repository/        # Spring Data JPA Repositories
│   │   │   ├── EmployeeRepository.java
│   │   │   ├── FeedbackRepository.java
│   │   │   └── AbsenceRequestRepository.java
│   │   ├── service/           # Business Logic Layer
│   │   │   ├── EmployeeService.java
│   │   │   ├── FeedbackService.java
│   │   │   └── AbsenceRequestService.java
│   │   ├── controller/        # REST Controllers
│   │   │   ├── EmployeeController.java
│   │   │   ├── FeedbackController.java
│   │   │   └── AbsenceRequestController.java
│   │   └── config/           # Configuration Classes
│   │       ├── CorsConfig.java
│   │       └── DataInitializer.java
│   ├── pom.xml
│   └── .env                  # Database configuration (not committed)
├── frontend/                 # Angular application
│   ├── src/
│   ├── package.json
│   └── angular.json
└── README.md
```

## 🗄️ Data Model

### Core Entities

#### Employee
- **Personal Info**: Name, email, phone, birth date, address
- **Employment**: Employee ID, position, department, hire date, salary
- **Relationships**: Manager hierarchy, direct reports
- **System Fields**: Active status, creation/update timestamps

#### Feedback
- **Content**: Title, content, rating (1-5), category, tags
- **Relationships**: Employee (recipient), feedback giver
- **Features**: Public/private, anonymous, AI enhancement support
- **Status**: Active, archived, deleted

#### AbsenceRequest
- **Details**: Type (vacation, sick, personal), start/end dates, reason
- **Workflow**: Status (pending, approved, rejected), manager approval
- **Features**: Half-day support, work delegation, emergency contacts
- **Audit**: Request timestamp, approval timestamp, manager comments

### Supporting Enums
- **EmploymentStatus**: ACTIVE, INACTIVE, TERMINATED
- **EmploymentType**: FULL_TIME, PART_TIME, CONTRACT, INTERN
- **FeedbackType**: POSITIVE, CONSTRUCTIVE, MANAGER_FEEDBACK, PERFORMANCE_REVIEW
- **FeedbackStatus**: ACTIVE, ARCHIVED, DELETED
- **AbsenceType**: VACATION, SICK_LEAVE, PERSONAL_LEAVE, MATERNITY_LEAVE, etc.
- **AbsenceStatus**: PENDING, APPROVED, REJECTED, CANCELLED

## 🚀 Getting Started

### Prerequisites

- **Java 17** or higher
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

The backend will start on `http://localhost:8080` with automatic sample data initialization.

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

### Employee Management
- `GET /api/employees` - Get all employees
- `GET /api/employees/{id}` - Get employee by ID
- `POST /api/employees` - Create new employee
- `PUT /api/employees/{id}` - Update employee
- `DELETE /api/employees/{id}` - Soft delete employee
- `GET /api/employees/search?name={name}` - Search by name
- `GET /api/employees/search?department={dept}` - Search by department
- `GET /api/employees/search?position={pos}` - Search by position
- `GET /api/employees/{id}/direct-reports` - Get direct reports
- `GET /api/employees/managers` - Get all managers
- `GET /api/employees/active` - Get active employees only

### Feedback Management
- `GET /api/feedback` - Get all feedback (role-based access)
- `GET /api/feedback/public` - Get public feedback only
- `GET /api/feedback/{id}` - Get feedback by ID
- `POST /api/feedback` - Create new feedback
- `PUT /api/feedback/{id}` - Update feedback
- `DELETE /api/feedback/{id}` - Archive feedback
- `GET /api/feedback/employee/{employeeId}` - Get feedback for employee
- `GET /api/feedback/search?content={text}` - Search feedback content
- `GET /api/feedback/category/{category}` - Get feedback by category
- `GET /api/feedback/rating/{rating}` - Get feedback by rating
- `GET /api/feedback/statistics/{employeeId}` - Get feedback statistics

### Absence Request Management
- `GET /api/absence-requests` - Get all absence requests
- `GET /api/absence-requests/{id}` - Get request by ID
- `POST /api/absence-requests` - Create new request
- `PUT /api/absence-requests/{id}` - Update request
- `DELETE /api/absence-requests/{id}` - Cancel request
- `POST /api/absence-requests/{id}/approve` - Approve request (managers)
- `POST /api/absence-requests/{id}/reject` - Reject request (managers)
- `GET /api/absence-requests/employee/{employeeId}` - Get requests for employee
- `GET /api/absence-requests/pending` - Get pending requests
- `GET /api/absence-requests/current` - Get current absences
- `GET /api/absence-requests/upcoming` - Get upcoming absences
- `GET /api/absence-requests/manager/{managerId}` - Get requests for manager approval

## 🎯 Key Features

### Role-Based Access Control
- **Manager**: Full access to department data, approval workflows
- **Co-worker**: Public feedback access, limited employee info
- **Employee**: Own data access, feedback submission

### Advanced Search & Filtering
- Multi-criteria search across all entities
- Case-insensitive text search
- Date range filtering
- Status and type filtering
- Pagination support

### Business Logic
- **Overlap Detection**: Prevents conflicting absence requests
- **Approval Workflow**: Manager approval for absence requests
- **Hierarchy Management**: Employee-manager relationships
- **Soft Delete**: Data preservation with logical deletion
- **Audit Trails**: Creation and update timestamps

### Data Validation
- Bean Validation annotations
- Email format validation
- Phone number pattern validation
- Date range validation
- Business rule enforcement

## 🗄️ Database

This project uses Supabase (PostgreSQL) for data persistence with the following features:

### Configuration
- Automatic schema generation with Hibernate
- Connection pooling with HikariCP
- Environment-based configuration
- DDL auto-update for development

### Sample Data
The `DataInitializer` automatically creates sample data on first startup:
- 6 employees with realistic hierarchy
- 4 feedback entries with different types
- 3 absence requests in various states

### Performance Features
- Indexed queries for common searches
- Lazy loading for relationships
- Pagination for large datasets
- Optimized JPQL queries

## 🔐 Security

- Environment variables for sensitive data
- CORS configuration for frontend integration
- Input validation with Bean Validation
- Secure credential management
- SQL injection prevention with JPQL

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
