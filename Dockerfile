# Use the OpenJDK 17 JDK base image
FROM openjdk:17-jdk-alpine

# Set the working directory
WORKDIR /app

# Copy the JAR file to the container
COPY --from=build /app/target/gestion-station-ski-1.1-SNAPSHOT.jar /app/gestion-station-ski.jar

# Expose port 8080
EXPOSE 8080

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/app/gestion-station-ski.jar"]
