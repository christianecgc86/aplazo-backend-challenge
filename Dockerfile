FROM eclipse-temurin:21
WORKDIR /app
COPY target/backend-challenge-demo-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-jar","app.jar"]