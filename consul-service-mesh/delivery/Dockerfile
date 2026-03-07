FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace
COPY . .
RUN ./gradlew --no-daemon clean bootJar

FROM eclipse-temurin:25-jre AS runtime

LABEL \
  org.opencontainers.image.title="delivery-service" \
  org.opencontainers.image.description="Delivery microservice for Consul Service Mesh and Kong Gateway example" \
  org.opencontainers.image.version="1.0" \
  org.opencontainers.image.source="https://github.com/segwarez/interview/consul-service-mesh/delivery" \
  org.opencontainers.image.licenses="Attribution-NonCommercial 4.0 International" \
  org.opencontainers.image.authors="Kamil Sajdok <kamil.sajdok@segwarez.com>"

WORKDIR /app

COPY --from=build /workspace/build/libs/delivery.jar /app/delivery.jar

ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/latest/download/opentelemetry-javaagent.jar \
    /app/opentelemetry-javaagent.jar

ENV JAVA_TOOL_OPTIONS="-XX:+UseZGC -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=25.0 -javaagent:/app/opentelemetry-javaagent.jar"

EXPOSE 8083
ENTRYPOINT ["java", "-jar", "/app/delivery.jar"]