FROM navikt/java:8
LABEL maintainer="Team Registre"

ADD "tps-forvalteren-application/target/tps-forvalteren.jar" /app/app.jar