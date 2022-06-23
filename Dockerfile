FROM gcr.io/distroless/java17
EXPOSE 8080

COPY build/libs/task-0.1.0-SNAPSHOT.jar ./run.jar

ENTRYPOINT ["java", "-jar", "run.jar"]
