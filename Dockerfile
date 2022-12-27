FROM openjdk:18
ARG JAR_FILE=target/*.jar
COPY ./target/Blog-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java", "-jar","/app.jar"]