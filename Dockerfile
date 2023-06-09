FROM openjdk:8-jdk-alpine

MAINTAINER whhc

WORKDIR /opt/java/project/nmap/release

ADD nmap.jar /opt/java/project/nmap/release/nmap.jar

EXPOSE 8080

ENTRYPOINT ["java", "-server", "-Xms512m", "-Xmx512m", "-jar", "/opt/java/project/nmap/release/nmap.jar"]


