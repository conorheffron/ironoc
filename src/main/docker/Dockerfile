FROM frolvlad/alpine-oraclejdk8:slim

VOLUME /tmp
ADD *.war app.war
RUN sh -c 'touch /app.war'

ENV RUN_FILE /run.sh
ADD run.sh ${RUN_FILE}
RUN dos2unix ${RUN_FILE}
RUN chmod +x ${RUN_FILE}

ENTRYPOINT [ "sh", "-c", "${RUN_FILE}" ]