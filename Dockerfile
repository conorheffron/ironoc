FROM eclipse-temurin:21-jdk

RUN --mount=type=secret,id=GIT_API_TOKEN,env=GIT_API_TOKEN
COPY target/*.war app.war
RUN sh -c 'touch /app.war'

ENV RUN_FILE /run.sh
COPY run.sh ${RUN_FILE}
RUN chmod +x ${RUN_FILE}

EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "${RUN_FILE}" ]
