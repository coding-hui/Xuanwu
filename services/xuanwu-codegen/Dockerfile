FROM docker.io/azul/zulu-openjdk:21-jre-latest

WORKDIR /xuanwu

ARG JAR_FILE=target/xuanwu-codegen-*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 9030

ENV TZ=Asia/Shanghai

ENTRYPOINT ["java", "-Dspring.config.additional-location=/xuanwu/config.yaml", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]