FROM openjdk:21
VOLUME /tmp
EXPOSE 8080
ARG JAR_FILE=build/libs/k8s-probe-test-v1.0.jar
ADD ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
