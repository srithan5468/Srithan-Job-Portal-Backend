# Use an official Maven image to build the project
# We use OpenJDK 17 because Spring Boot 3.x uses Java 17+
FROM maven:3.8-openjdk-17-slim AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the build file (pom.xml) and source code
COPY pom.xml .
COPY src /app/src

# Build the application
# We use the Maven command to clean, install, and skip tests
RUN mvn clean install -DskipTests

# --- Second stage: Create a smaller, more secure runtime image ---
# Use a slim JDK image only for running the app (not building)
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built JAR file from the build stage
# Ensure JobportalBackendApplication.jar matches the name Maven creates
COPY --from=build /app/target/JobportalBackendApplication.jar JobportalBackendApplication.jar

# Expose the port Spring Boot runs on
EXPOSE 8080

# Command to run the application when the container starts
CMD ["java", "-jar", "JobportalBackendApplication.jar"]