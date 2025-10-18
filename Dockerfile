FROM eclipse-temurin:25-alpine-3.22

COPY target/*.war app.war
RUN sh -c 'touch /app.war'

ENV RUN_FILE /run.sh
COPY run.sh ${RUN_FILE}
RUN chmod +x ${RUN_FILE}

EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "${RUN_FILE}" ]
