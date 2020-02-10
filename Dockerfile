FROM navikt/java:8
LABEL maintainer="Team Registre"

ADD "tps-forvalteren-app-nais/target/app-exec.jar" /app/app.jar

EXPOSE 8080
