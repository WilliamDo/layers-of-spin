FROM tomcat:9-jdk8

WORKDIR /usr/src/app

COPY . .

RUN ./mvnw package

COPY ./target/TableTennisLeagueManager-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ttlm.war