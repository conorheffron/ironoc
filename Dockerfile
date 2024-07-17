FROM eclipse-temurin:21-jdk

VOLUME /tmp
ADD *.war app.war
RUN sh -c 'touch /app.war'

ENV RUN_FILE /run.sh
ADD run.sh ${RUN_FILE}
RUN chmod +x ${RUN_FILE}

ENTRYPOINT [ "sh", "-c", "${RUN_FILE}" ]