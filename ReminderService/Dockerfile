# reminder service - dockerization
# ================================
#
FROM java:8-jre
EXPOSE 8081
ADD ./target/ReminderService-0.0.1-SNAPSHOT.jar /usr/src/ReminderService-0.0.1-SNAPSHOT.jar
WORKDIR usr/src
ENTRYPOINT ["java","-jar","ReminderService-0.0.1-SNAPSHOT.jar"]
