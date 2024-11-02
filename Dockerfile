# Stage 1: Build the application
FROM openjdk:17-jdk-alpine AS build

# Set the working directory
WORKDIR /app

# Copy the project files into the container
COPY . /app

# Build the application (replace with your actual build command, e.g., Maven or Gradle)
# For Maven, assuming a pom.xml is in the root of the project:
RUN ./mvnw clean package -DskipTests

# Stage 2: Create the final image
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file from the build stage
COPY --from=build /app/target/gestion-station-ski-1.1-SNAPSHOT.jar /app/gestion-station-ski.jar

# Expose port 8080
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/gestion-station-ski.jar"]
