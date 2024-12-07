FROM eclipse-temurin:17-jdk-alpine
RUN mkdir /app
EXPOSE 3002

ARG JAR_FILE

COPY /target/${JAR_FILE} /app/app.jar
WORKDIR /app
ENTRYPOINT ["java", "-Dspring.datasource.url=${EVENTS_URL_DATABASE}", "-Dspring.datasource.username=${DATABASE_USERNAME}", "-Dspring.datasource.password=${DATABASE_PASSWORD}", "-Dspring.mail.username=${SPRING_MAIL_USERNAME}", "-Dspring.mail.password=${SPRING_MAIL_PASSWORD}", "-Djwt.secret=${JWT_SECRET}", "-Dazure.storage.connection-string=${AZURE_STORAGE_CONNECTION_STRING}", "-jar", "app.jar"]