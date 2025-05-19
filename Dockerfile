FROM eclipse-temurin:21-jdk-alpine-3.21

COPY target/*.war app.war
RUN sh -c 'touch /app.war'

ENV RUN_FILE /run.sh
COPY run.sh ${RUN_FILE}
RUN chmod +x ${RUN_FILE}

EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "${RUN_FILE}" ]
