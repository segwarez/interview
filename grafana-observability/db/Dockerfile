FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace
COPY . .
RUN ./gradlew --no-daemon clean bootJar

FROM eclipse-temurin:25-jre AS runtime

LABEL \
  org.opencontainers.image.title="observability-db" \
  org.opencontainers.image.description="Database service consuming events from Kafka for Grafana observability example" \
  org.opencontainers.image.version="1.0" \
  org.opencontainers.image.source="https://github.com/segwarez/interview/grafana-observability/db" \
  org.opencontainers.image.licenses="Attribution-NonCommercial 4.0 International" \
  org.opencontainers.image.authors="Kamil Sajdok <kamil.sajdok@segwarez.com>"

WORKDIR /app

COPY --from=build /workspace/build/libs/db.jar /app/db.jar

ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.25.0/opentelemetry-javaagent.jar opentelemetry-javaagent.jar
ADD https://github.com/grafana/otel-profiling-java/releases/download/v1.1.0/pyroscope-otel.jar pyroscope-otel.jar

ENV OTEL_SERVICE_NAME=db
ENV OTEL_TRACES_EXPORTER=otlp
ENV OTEL_METRICS_EXPORTER=otlp
ENV OTEL_LOGS_EXPORTER=otlp
ENV OTEL_JAVAAGENT_EXTENSIONS=./pyroscope-otel.jar

ENV PYROSCOPE_APPLICATION_NAME=db
ENV PYROSCOPE_FORMAT=jfr
ENV OTEL_PYROSCOPE_ADD_PROFILE_URL=false
ENV OTEL_PYROSCOPE_ADD_PROFILE_BASELINE_URL=false

ENV JAVA_TOOL_OPTIONS="-XX:+UseZGC \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=25.0 \
    -javaagent:./opentelemetry-javaagent.jar"

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/db.jar"]