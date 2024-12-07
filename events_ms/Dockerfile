FROM eclipse-temurin:17-jdk-alpine
RUN mkdir /app
EXPOSE 3002

ARG JAR_FILE

COPY /target/${JAR_FILE} /app/app.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "app.jar"]