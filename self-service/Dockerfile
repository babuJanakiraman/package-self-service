FROM openjdk:17-jdk-slim

ADD target/self-service-1.0.jar self-service.jar

EXPOSE 8200

ENTRYPOINT ["java", "-jar", "self-service.jar"]