FROM maven:3.5.4-jdk-8-alpine as builder

WORKDIR /home

COPY . .
RUN mvn install:install-file -Dfile=./libs/java-sdk-5.2.3.jar -DgroupId=com.constantcontact -DartifactId=java-sdk -Dversion=5.2.3 -Dpackaging=jar
RUN mvn install:install-file -Dfile=./libs/java-components-5.2.3.jar -DgroupId=com.constantcontact -DartifactId=java-components -Dversion=5.2.3 -Dpackaging=jar

RUN mvn clean package

FROM openjdk:8u171-jre-alpine

COPY --from=builder /home/src/main/resources/application.properties application.properties
COPY --from=builder /home/target/cdh-apicc-1.0.0.jar cdh-apicc-1.0.0.jar

ENTRYPOINT ["sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -jar /cdh-apicc-1.0.0.jar" ]
