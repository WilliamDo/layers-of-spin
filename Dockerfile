FROM tomcat:9-jdk8

COPY target/TableTennisLeagueManager-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/ttlm.war