# 第一阶段：构建
FROM openjdk:25-jdk-slim AS builder

ENV JAVA_HOME=/usr/local/openjdk-25
ENV PATH=${JAVA_HOME}/bin:${PATH}

WORKDIR /app

RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

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
