# Stage 1: Build the application using Maven
FROM maven:3.8.4-openjdk-17 as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application using a lightweight Java image
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
