FROM openjdk:17-jdk-slim

ADD target/shipping-service-1.0.jar shipping-service.jar

EXPOSE 8100

ENTRYPOINT ["java", "-jar", "shipping-service.jar"]