FROM eclipse-temurin:25-jdk AS build
WORKDIR /workspace
COPY . .
RUN ./gradlew --no-daemon clean bootJar

FROM eclipse-temurin:25-jre AS runtime

LABEL \
  org.opencontainers.image.title="warehouse-service" \
  org.opencontainers.image.description="Warehouse microservice for Consul Service Mesh and Kong Gateway example" \
  org.opencontainers.image.version="1.0" \
  org.opencontainers.image.source="https://github.com/segwarez/interview/consul-service-mesh/warehouse" \
  org.opencontainers.image.licenses="Attribution-NonCommercial 4.0 International" \
  org.opencontainers.image.authors="Kamil Sajdok <kamil.sajdok@segwarez.com>"

WORKDIR /app

COPY --from=build /workspace/build/libs/warehouse.jar /app/warehouse.jar

ADD https://github.com/open-telemetry/opentelemetry-java-instrumentation/releases/download/v2.25.0/opentelemetry-javaagent.jar opentelemetry-javaagent.jar
ADD https://github.com/grafana/otel-profiling-java/releases/download/v1.1.0/pyroscope-otel.jar pyroscope-otel.jar

ENV OTEL_SERVICE_NAME=warehouse
ENV OTEL_TRACES_EXPORTER=otlp
ENV OTEL_METRICS_EXPORTER=otlp
ENV OTEL_LOGS_EXPORTER=otlp
ENV OTEL_JAVAAGENT_EXTENSIONS=./pyroscope-otel.jar

ENV PYROSCOPE_APPLICATION_NAME=warehouse
ENV PYROSCOPE_FORMAT=jfr
ENV OTEL_PYROSCOPE_ADD_PROFILE_URL=false
ENV OTEL_PYROSCOPE_ADD_PROFILE_BASELINE_URL=false

ENV JAVA_TOOL_OPTIONS="-XX:+UseZGC -XX:MaxRAMPercentage=75.0 -XX:InitialRAMPercentage=25.0 -javaagent:./opentelemetry-javaagent.jar"

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/warehouse.jar"]