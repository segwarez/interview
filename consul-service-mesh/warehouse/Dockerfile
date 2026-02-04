FROM eclipse-temurin:25-jre-ubi10-minimal

LABEL \
  org.opencontainers.image.title="warehouse-service" \
  org.opencontainers.image.description="Warehouse microservice for Consul Service Mesh and Kong Gateway example" \
  org.opencontainers.image.version="1.0" \
  org.opencontainers.image.source="https://github.com/segwarez/interview/consul-service-mesh/warehouse" \
  org.opencontainers.image.licenses="Attribution-NonCommercial 4.0 International" \
  org.opencontainers.image.authors="Kamil Sajdok <kamil.sajdok@segwarez.com>"

WORKDIR /app

COPY build/libs/warehouse.jar warehouse.jar

EXPOSE 8082

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "/app/warehouse.jar"]