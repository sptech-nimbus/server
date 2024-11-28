FROM eclipse-temurin:17-jdk-alpine
RUN mkdir /app
EXPOSE 3000
ARG JAR_FILE
ADD /target/${JAR_FILE} /app/app.jar
WORKDIR /app
ENTRYPOINT ["java", "-jar", "app.jar"]