FROM eclipse-temurin:21-jdk

VOLUME /tmp
#for local
#COPY target/*.war app.war
#for CI/CD
ARG JAR_FILE=ironoc-personal-portfolio-3.1.8.war
COPY ${JAR_FILE} app.war
RUN sh -c 'touch /app.war'

ENV RUN_FILE /run.sh
COPY run.sh ${RUN_FILE}
RUN chmod +x ${RUN_FILE}

EXPOSE 80

ENTRYPOINT [ "sh", "-c", "${RUN_FILE}" ]
