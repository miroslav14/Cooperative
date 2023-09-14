FROM openjdk:20
ARG JAR_FILE
COPY ${JAR_FILE} dockerFile_jar.jar
ENTRYPOINT ["java","-jar","/dockerFile_jar.jar"]
