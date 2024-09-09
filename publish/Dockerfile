FROM eclipse-temurin:21-jdk

VOLUME /tmp

#for CI/CD/ Docker ghcr image
ADD *.war app.war
RUN sh -c 'touch /app.war'

ENV RUN_FILE /run.sh
ADD run.sh ${RUN_FILE}
RUN chmod +x ${RUN_FILE}

EXPOSE 8080

ENTRYPOINT [ "sh", "-c", "${RUN_FILE}" ]
