FROM openjdk:17-jdk-alpine
EXPOSE 8089
VOLUME /tmp
ENTRYPOINT ["java","-jar","/td.jar"]
