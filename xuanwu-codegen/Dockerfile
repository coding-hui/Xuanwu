FROM azul/zulu-openjdk:21-jre-latest

WORKDIR /xuanwu

ARG JAR_FILE=target/xuanwu-codegen-*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 9030

ENV TZ=Asia/Shanghai JAVA_OPTS="-Xms128m -Xmx256m -Dspring.config.additional-location=/xuanwu/config.yaml -Djava.security.egd=file:/dev/./urandom"

CMD sleep 60; java $JAVA_OPTS -jar app.jar