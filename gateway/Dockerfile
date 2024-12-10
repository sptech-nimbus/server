# syntax=docker/dockerfile:1
FROM openjdk:17-jdk-slim
RUN mkdir /app
EXPOSE 8080

ARG JAR_FILE

ADD /target/${JAR_FILE} /app/app.jar
WORKDIR /app

ENTRYPOINT ["java", "-jar", "app.jar"]