# 构建阶段
FROM gradle:8.14.1-jdk17 AS build
WORKDIR /workspace
COPY . .
RUN gradle build --no-daemon

# 运行阶段
FROM amazoncorretto:17-alpine-jdk
COPY --from=build /workspace/build/libs/easyweibo-*-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]