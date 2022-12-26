# Multi Stage Build

#Base Image and name stage as "builder"
FROM maven:3-openjdk-18 AS builder

#Create App Directory inside container
WORKDIR /ik/app/src/

#Copy files
COPY src ./
COPY pom.xml ../

RUN mvn -f /ik/app/pom.xml clean package

#### 2nd Stage ####

FROM openjdk:18

WORKDIR /ik/lib/

COPY --from=builder /ik/app/target/Blog-0.0.1-SNAPSHOT.jar ./my-blog-dockerized.jar

# Expose the port to the inner container communication network
EXPOSE 8081

ENTRYPOINT ["java", "-jar","/ik/lib/my-blog-dockerized.jar"]