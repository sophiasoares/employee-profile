# Employee Management System

A single-page HR application supporting role-based data access for employee profiles, feedback collection, and absence requests with AI-powered feedback enhancement.

## 🌐 Live Demo

Visit the live application: [https://employee-profile-system.onrender.com](https://employee-profile-system.onrender.com)

**Note**: The free tier may take 30-60 seconds to wake up if it hasn't been used recently.

## 🏗️ Tech Stack

- **Frontend**: Angular 20+ with TypeScript
- **Backend**: Spring Boot 3.5+ with Java 17
- **Database**: Supabase (PostgreSQL)
- **AI Enhancement**: OpenRouter API with Mistral 7B
- **Deployment**: Docker multi-stage build
- **Build Tools**: Maven (Backend), Angular CLI (Frontend)

## 🎯 Key Features

The requirements were short and didn't provide many details, so I had to make some assumptions and design the system based on my experience and logic. For example: "As an employee, I can request an absence". I didn't know if ONLY an employee could request an absence, but it made sense to me to allow managers and co-workers to request absences as well, so i implemented it like that. Or also: "As a co-worker, I can leave feedback". In my application, a manager can also leave feedback, as they are kinda like the administrator and have full access to everything. I wasn't completely sure about the practical difference between a co-worker and an employee, so I assumed something like: a co-worker works in the HR department and an employee is a regular employee. so the co-worker has access to the employee list with non-sensitive data, while the employee can only see their own profile data.

### Role-Based Access Control
- **Manager**: Full access to all employee data, can edit profiles, see/create absences, view/give feedback
- **Co-worker**: Can view all employee profiles (only non-sensitive data), see/create absences, view/give feedback to colleagues
- **Employee**: Can only view their own profile data, see their absences, see feedback given to them, request absences
- **Search Functionality**: Real-time search by name (only for managers and co-workers)
- **Feedback Functionality**: feedback is given to employees by managers and co-workers
- **Absence Functionality**: absences are requested by everyone. Managers and co-workers can see all requests and employees can see their own requests.

### AI-Powered Feedback Enhancement
- **Smart Enhancement**: i used OpenRouter API with Mistral 7B model to improve feedback quality and convert casual feedback into professional, constructive language

## 🗄️ Data Models

### Core Entities

#### Employee
Represents an employee with personal information, employment details, and role-based access control.

#### Feedback
Is a feedback given to an employee by a co-worker or manager.

#### AbsenceRequest
Represents a time-off request with an absence type, reason and date range.

### Enums
- **EmploymentType**: Different employment arrangements (full-time, part-time, contract, etc.)
- **FeedbackType**: Categories of feedback (positive, constructive, manager feedback, performance reviews)
- **AbsenceType**: different absence types (from vacation to remote work)
- **EmployeeRole**: Role of an employee to control access to data and features (manager, co-worker, employee)

## 🏛️ Architecture Decisions

- I chose Angular, Spring Boot and Supabase as tech stack because I am familiar with them and they are easy to use.
- In the backend I implemented clean separation with controllers, services, and repositories (the flow goes: controller -> service -> repository -> database)
- Used JPA/Hibernate to help me create the database schema and object-relational mapping
- Used RESTful APIs to provide a simple, predictable endpoint for each operation
- Used environment variables for sensitive data
- I chose not to use a package for state managament in the frontend (like NgRx) because i wanted to keep it simple and using signals was enough for this application
- Used services in the frotnend to handle business logic and data access.
- The project has MVVM architecture with components, services, and models. Angular works very well with this architecture, so i thought it was a good and straighforward choice for the project.
- I used AI (Windsurf) to help me with some refactoring, to populate the database, provide me with ideas for models properties (example: role, department, address, etc. for the Employee model)
- i also used AI to help me connect OpenRouter API with the application so that i could have AI-powered feedback enhancement

## Future enhancements
There are many things I would develop to complement this project. Some of them are:
- Add tests for each component, endpoints, services, methods
- Implement create and delete operations for employees
- Implement update and delete for feeebacm and absence requests
- implement approval/rejection for absence requests
- Increase search capabilities (search by phone number, address, etc.)
- add filters (filter by role, department, etc.)
- I hardcoded 3 users (one manager, one co-worker, one employee) for the role switcher, so i would do it properly and add authentication, login, etc.
- I would add more fields to the models, like emergency contacts, updatedAt, etc.
- improve and add more validation rules
- i would use a state management package (like NgRx) to manage the state of the frontend
- probably improve documentation

The goal was to create a system that's easy to understand, extend, and maintain while providing real business value through role-based access control and AI-enhanced feedback.

## 🚀 How to Run

I hosted the application on Render. You can find it here: [https://employee-profile-system.onrender.com](https://employee-profile-system.onrender.com), but if you want to run it locally, follow the instructions below.

### Prerequisites
- **Java 17** or higher
- **Node.js 18+** and npm
- **Maven 3.6+**
- **Supabase account** (for database)
- **OpenRouter API token** (for AI enhancement)
- **Docker** (for containerization)

### Docker Setup

1. Build the Docker image:
   ```bash
   docker build -t employee-profile-system .
   ```

2. Run the Docker container:
   ```bash
   docker run -p 8080:8080 employee-profile-system
   ```

3. Access the application at `http://localhost:8080`

### Backend Setup

1. Navigate to the backend directory:
   ```bash
   cd backend
   ```

2. Create a `.env` file with your credentials:
   ```env
   SUPABASE_DB_HOST=db.your-project-ref.supabase.co
   SUPABASE_DB_PORT=5432
   SUPABASE_DB_NAME=postgres
   SUPABASE_DB_USER=postgres
   SUPABASE_DB_PASSWORD=your-database-password
   OPENROUTER_API_TOKEN=your-openrouter-token
   ```

3. Install dependencies and run:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

Backend starts on `http://localhost:8080` with automatic sample data initialization.

### Frontend Setup

1. Update line 12 in `frontend/src/app/services/employee.service.ts` to use local API URL. Change it to:
  ```private readonly apiUrl = 'http://localhost:8080/api';``` 

2. Navigate to the frontend directory:
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

Frontend starts on `http://localhost:4200`
