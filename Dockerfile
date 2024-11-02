# Step 1: Use an official Maven image with JDK 17 to build the app
FROM maven:3.8.6-eclipse-temurin-17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the pom.xml file and the source code to the working directory
COPY pom.xml .
COPY src ./src

# Step 2: Build the Spring Boot application
RUN mvn clean package -DskipTests

# Step 3: Use an official OpenJDK image to run the app
FROM openjdk:17-jdk-slims

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/gestion-station-ski-1.1-SNAPSHOT.jar /app/gestion-station-ski.jar

# Expose the port that the Spring Boot app runs on
EXPOSE 8080

# Command to run the Spring Boot app
ENTRYPOINT ["java", "-jar", "/app/gestion-station-ski.jar"]
