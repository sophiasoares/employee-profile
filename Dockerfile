# Multi-stage build
FROM node:20-alpine AS frontend-build

# Build Angular frontend
WORKDIR /app/frontend
COPY frontend/package*.json ./
RUN npm ci
COPY frontend/ ./
RUN npm run build

# Java backend stage
FROM openjdk:21-jdk-slim

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Copy backend source
WORKDIR /app
COPY backend/ ./

# Copy built frontend to Spring Boot static resources
# Angular 20+ outputs to dist/employee-profile/browser by default
COPY --from=frontend-build /app/frontend/dist/employee-profile/browser/ ./src/main/resources/static/

# Build Spring Boot application
RUN mvn clean package -DskipTests

# Expose port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/backend-0.0.1-SNAPSHOT.jar"]
