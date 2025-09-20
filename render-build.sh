#!/bin/bash

echo "🚀 Render Build Script Starting..."

# Install Node.js dependencies and build Angular
echo "📦 Installing frontend dependencies..."
cd frontend
npm ci

echo "🏗️ Building Angular for production..."
npm run build

# Copy built Angular app to Spring Boot static resources
echo "📁 Moving frontend build to backend..."
mkdir -p ../backend/src/main/resources/static
rm -rf ../backend/src/main/resources/static/*
cp -r dist/employee-profile/* ../backend/src/main/resources/static/

# Build Spring Boot application
echo "☕ Building Spring Boot application..."
cd ../backend
mvn clean package -DskipTests

echo "✅ Render build complete!"
