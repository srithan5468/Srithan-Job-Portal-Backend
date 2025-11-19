# Use an official Maven image to build the project
FROM maven:3.8-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml .
COPY src /app/src

# FIX: Use 'package' instead of 'install' and skip tests for production build
RUN mvn clean package -DskipTests

# --- Second stage: Create the smaller runtime image ---
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the built JAR file from the build stage
COPY --from=build /app/target/JobportalBackendApplication.jar JobportalBackendApplication.jar

EXPOSE 8080

# Command to run the application when the container starts
CMD ["java", "-jar", "JobportalBackendApplication.jar"]