# BUILDER
FROM gradle:7.6.1-jdk17 AS builder
ADD ./ /academy
WORKDIR /academy
RUN gradle bootJar --no-daemon

# RUNNING
FROM azul/zulu-openjdk-alpine:17-jre
WORKDIR /academy
COPY --from=builder /academy/build/libs/academy-show-0.0.1-SNAPSHOT.jar acaedmy.jar
CMD java -Dspring.profiles.active=default -Dserver.port=$PORT $JAVA_OPTS -Dspring.config.location=application.yaml -jar acaedmy.jar
