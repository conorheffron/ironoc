FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/ironoc-1.4.0.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar /app.jar" ]