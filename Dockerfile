FROM maven:3.5.3-jdk-8-slim AS build

WORKDIR /var/lib/owl2vowl
ADD pom.xml .
RUN ["mvn", "clean"]

ADD src src
RUN ["mvn", "package", "-DskipTests", "-P", "war-release"]


FROM java:8-jre-alpine

WORKDIR /owl2vowl
COPY --from=build /var/lib/owl2vowl/target/owl2vowl.war .
CMD java -jar owl2vowl.war

EXPOSE 8080