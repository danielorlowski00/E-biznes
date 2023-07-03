FROM gradle:7.2.0-jdk17
COPY --chown=gradle:gradle . /app

WORKDIR /app

RUN gradle buildFatJar --no-daemon

FROM openjdk:17-jdk
COPY --from=0 /app/build/libs/*.jar app.jar

EXPOSE 8080:8080

ENTRYPOINT ["java","-jar","app.jar"]