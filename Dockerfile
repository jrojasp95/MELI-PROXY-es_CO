FROM adoptopenjdk/openjdk11:latest
LABEL maintainer="jrojpin@gmail.com"

COPY /target/*jar /opt/target/app.jar
WORKDIR /opt/target


ENTRYPOINT ["java", "-jar", "app.jar"]