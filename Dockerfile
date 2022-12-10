FROM openjdk:18
EXPOSE 8081
LABEL MAINTAINER = "Ikechi Ucheagwu "ikechiucheagwu@gmail.com""
COPY ./target/Blog-0.0.1-SNAPSHOT.jar /opt/myblog.jar
COPY . /opt/
ENTRYPOINT ["java", "-jar","/opt/myblog.jar", "--server.port=8081", "--spring.config.location=file:/opt/src/main/resources/"]