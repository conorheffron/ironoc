FROM frolvlad/alpine-oraclejdk8:slim

VOLUME /tmp
ADD *.war app.war
RUN sh -c 'touch /app.war'

ENTRYPOINT [ "sh", "-c", "java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /app.war" ]