FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/deops_project-1.0.jar /app/deops_project.jar


EXPOSE 8080

CMD ["java", "-jar", "deops_project.jar"]