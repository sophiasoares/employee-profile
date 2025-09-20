#!/bin/bash

echo "🔨 Building Employee Management System for Production..."

# Build Angular frontend
echo "📦 Building Angular frontend..."
cd frontend
npm ci
npm run build

# Copy Angular build to Spring Boot static resources
echo "📁 Copying frontend to backend..."
rm -rf ../backend/src/main/resources/static/*
cp -r dist/employee-profile/* ../backend/src/main/resources/static/

# Build Spring Boot backend
echo "🏗️ Building Spring Boot backend..."
cd ../backend
mvn clean package -DskipTests

echo "✅ Build complete! Ready for deployment."
