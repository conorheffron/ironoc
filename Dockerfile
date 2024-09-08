FROM eclipse-temurin:21-jdk

#for local
#ADD target/*.war app.war
#for CI/CD
ADD *.war app.war
RUN sh -c 'touch /app.war'

ENV RUN_FILE /run.sh
ADD run.sh ${RUN_FILE}
RUN chmod +x ${RUN_FILE}

EXPOSE 80

ENTRYPOINT [ "sh", "-c", "${RUN_FILE}" ]
