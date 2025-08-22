# syntax=docker/dockerfile:1.7
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/auth-service-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]