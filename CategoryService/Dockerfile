# category service - dockerization
# ================================
#
FROM java:8-jre
EXPOSE 8083
ADD ./target/CategoryService-0.0.1-SNAPSHOT.jar /usr/src/CategoryService-0.0.1-SNAPSHOT.jar
WORKDIR usr/src
ENTRYPOINT ["java","-jar","CategoryService-0.0.1-SNAPSHOT.jar"]
