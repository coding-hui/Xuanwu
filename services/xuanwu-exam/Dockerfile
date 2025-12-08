# 第一阶段：构建
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /app

# 拷贝源码
COPY . .

# 构建 xuanwu-exam
RUN mvn clean package -pl services/xuanwu-exam -am -DskipTests

# 第二阶段：运行
FROM openjdk:25-jdk-slim

WORKDIR /xuanwu

# 从 builder 阶段拷贝构建产物
# 注意：路径需要根据实际构建结果调整，通常是 services/xuanwu-exam/target/xuanwu-exam-*.jar
COPY --from=builder /app/services/xuanwu-exam/target/xuanwu-exam-*.jar app.jar

EXPOSE 8080

ENV TZ=Asia/Shanghai

ENTRYPOINT ["java", "-Dspring.config.additional-location=/xuanwu/config.yaml", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
