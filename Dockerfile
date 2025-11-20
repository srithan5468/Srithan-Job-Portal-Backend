# Stage 1: Build using Maven with JDK 17
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy Maven files and source
COPY pom.xml .
COPY src ./src
COPY mvnw .
COPY .mvn .mvn

RUN chmod +x mvnw

# Build the application
RUN ./mvnw clean package -DskipTests

# Stage 2: Create a lightweight JRE image
FROM eclipse-temurin:17-jre

WORKDIR /app

# Copy the jar file from build stage
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Run the spring boot app
CMD ["java", "-jar", "app.jar"]
