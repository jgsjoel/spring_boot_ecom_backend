FROM openjdk:21-jdk

WORKDIR /app

COPY target/ddAPI-0.0.1-SNAPSHOT.jar /app/ddAPI-0.0.1-SNAPSHOT.jar
COPY src/main/resources/application.properties /app/application.properties
COPY src/main/resources/secret.json /app/secret.json

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "ddAPI-0.0.1-SNAPSHOT.jar", "--spring.config.location=file:/app/application.properties"]