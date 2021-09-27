FROM openjdk:11
WORKDIR /app
COPY target/demo-working-0.0.1-SNAPSHOT.jar /app/demo-working-0.0.1-SNAPSHOT.jar
CMD ["java", "-jar","demo-working-0.0.1-SNAPSHOT.jar"]