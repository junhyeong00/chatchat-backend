FROM openjdk:17

COPY build/libs/*[^plain].jar app.jar

ENTRYPOINT ["java", "-Dspring.profiles.active=production", "-jar", "./app.jar"]
