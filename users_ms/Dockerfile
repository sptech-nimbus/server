# syntax=docker/dockerfile:1
FROM eclipse-temurin:17-jdk-alpine
RUN mkdir /app
EXPOSE 3000

ARG JAR_FILE

RUN --mount=type=secret,id=USERS_URL_DATABASE,env=USERS_URL_DATABASE \
    --mount=type=secret,id=DATABASE_USERNAME,env=DATABASE_USERNAME \
    --mount=type=secret,id=DATABASE_PASSWORD,env=DATABASE_PASSWORD \
    --mount=type=secret,id=SPRING_MAIL_USERNAME,env=SPRING_MAIL_USERNAME \
    --mount=type=secret,id=SPRING_MAIL_PASSWORD,env=SPRING_MAIL_PASSWORD \
    --mount=type=secret,id=JWT_SECRET,env=JWT_SECRET \
    --mount=type=secret,id=AZURE_STORAGE_CONNECTION_STRING,env=AZURE_STORAGE_CONNECTION_STRING \
    echo "Secrets loaded"

COPY /target/${JAR_FILE} /app/app.jar

WORKDIR /app

ENTRYPOINT ["java", "-jar", "app.jar"]