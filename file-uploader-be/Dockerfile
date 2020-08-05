FROM openjdk:14-alpine
COPY target/file-uploader.jar file-uploader.jar
RUN mkdir ./files

ENV SPRING_PROFILE local

ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "-jar", "file-uploader.jar"]
