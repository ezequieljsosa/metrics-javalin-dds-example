# syntax = docker/dockerfile:1.2
#
# Build stage
#
FROM maven:3.8.6-openjdk-18 AS build
COPY . .
RUN mvn clean package assembly:single -DskipTests

#
# Package stage
#
FROM openjdk:17-jdk-slim
COPY --from=build /target/metrics-example-1.0-SNAPSHOT-jar-with-dependencies.jar app.jar
# ENV PORT=8080
EXPOSE 8080
CMD ["java","-classpath","app.jar","ar.edu.utn.dds.k3003.TransferenciasApp"]
