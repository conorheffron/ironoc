FROM eclipse-temurin:8-jdk
MAINTAINER Conor Heffron <conor.heffron@gmail.com>

RUN \
  java -Dserver.port=$PORT $JAVA_OPTS -jar target/ironoc-1.4.4.war

EXPOSE 8080
