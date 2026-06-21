FROM quay.io/debezium/connect:3.4.0.Final

USER root

RUN microdnf install -y curl tar gzip && microdnf clean all \
    && mkdir -p /kafka/connect/debezium-connector-jdbc \
    && curl -fsSL \
       "https://repo1.maven.org/maven2/io/debezium/debezium-connector-jdbc/3.4.0.Final/debezium-connector-jdbc-3.4.0.Final-plugin.tar.gz" \
       | tar -xz --strip-components=1 -C /kafka/connect/debezium-connector-jdbc \
    && curl -fsSL \
       "https://repo1.maven.org/maven2/org/postgresql/postgresql/42.7.11/postgresql-42.7.11.jar" \
       -o /kafka/connect/debezium-connector-jdbc/postgresql-42.7.11.jar

USER kafka