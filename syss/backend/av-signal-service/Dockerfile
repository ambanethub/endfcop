FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY target/av-signal-service-*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]