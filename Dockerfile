# Use an official Maven image to build the project
FROM maven:3.8-openjdk-17-slim AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the build file (pom.xml) and source code
COPY pom.xml .
COPY src /app/src
# Copy the Maven Wrapper files (crucial for local execution)
COPY mvnw .
COPY .mvn .mvn

# FIX: Grant execution permission to the Maven Wrapper file
RUN chmod +x mvnw

# Build the application using the local wrapper
# This is the most reliable build command in this environment
RUN ./mvnw clean package -DskipTests

# --- Second stage: Create a smaller, more secure runtime image ---
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/JobportalBackendApplication.jar JobportalBackendApplication.jar

# Expose the port Spring Boot runs on
EXPOSE 8080

# Command to run the application when the container starts
CMD ["java", "-jar", "JobportalBackendApplication.jar"]