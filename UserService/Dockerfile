FROM java:8-jre
EXPOSE 8080
ADD ./target/UserService-0.0.1-SNAPSHOT.jar /usr/src/UserService-0.0.1-SNAPSHOT.jar
WORKDIR usr/src
ENTRYPOINT ["java","-jar","UserService-0.0.1-SNAPSHOT.jar"]
