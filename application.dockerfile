
FROM openjdk:21
WORKDIR /LeisureUp-BE
ARG JAR_FILE=build/libs/LeisureUp-BE-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-jar", "/LeisureUp-BE/application.jar"]
