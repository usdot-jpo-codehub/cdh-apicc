FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD ./src/main/resources/application.properties application.properties
ADD ./target/cdh-apicc-1.0.0.jar cdh-apicc-1.0.0.jar
ENV JAVA_OPTS=""
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /cdh-apicc-1.0.0.jar" ]