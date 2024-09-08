FROM eclipse-temurin:21-jdk

VOLUME /tmp
#for local
#COPY target/*.war app.war
#for CI/CD
COPY *.war app.war
RUN sh -c 'touch /app.war'

ENV RUN_FILE /run.sh
COPY run.sh ${RUN_FILE}
RUN chmod +x ${RUN_FILE}

EXPOSE 80

ENTRYPOINT [ "sh", "-c", "${RUN_FILE}" ]
