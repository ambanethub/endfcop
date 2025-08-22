# syntax=docker/dockerfile:1.7
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /workspace
COPY ../../pom.xml ./
COPY ../shared ../shared
COPY ./ ./auth-service
RUN --mount=type=cache,target=/root/.m2 mvn -q -f auth-service/pom.xml -pl auth-service -am -DskipTests package

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /workspace/auth-service/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]