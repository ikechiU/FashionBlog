FROM openjdk:18
EXPOSE 8081
ADD target/my-fashion-blog.jar my-fashion-blog.jar
ENTRYPOINT ["java", "-jar","/my-fashion-blog.jar"]