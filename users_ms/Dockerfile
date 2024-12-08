FROM eclipse-temurin:17-jdk-alpine
RUN mkdir /app
EXPOSE 3000

ARG JAR_FILE

COPY .env /app/.env
COPY /target/${JAR_FILE} /app/app.jar
WORKDIR /app

RUN apk add --no-cache bash
RUN echo "source /app/.env" >> /etc/profile

ENTRYPOINT ["bash", "-c", "source /app/.env && java -jar app.jar"]