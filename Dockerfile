FROM openjdk:19
EXPOSE 8080
ARG JAR_FILE=target/transaction_service-1.0-SNAPSHOT.jar
WORKDIR /opt/app
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]