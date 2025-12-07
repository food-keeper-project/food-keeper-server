FROM azul/zulu-openjdk-alpine:21-latest AS builder
WORKDIR /app

COPY . .

RUN chmod +x ./gradlew
RUN ./gradlew clean build -x test

FROM azul/zulu-openjdk-alpine:21-latest
WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]