# Stage 1: Build using Maven
FROM maven:3.8.8-openjdk-17-slim AS build

WORKDIR /app

COPY pom.xml .
COPY src /app/src
COPY mvnw .
COPY .mvn .mvn

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests

# Stage 2: Smaller runtime container
FROM openjdk:17-jdk-slim

WORKDIR /app

# Copy the jar built in stage 1
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

CMD ["java", "-jar", "app.jar"]
