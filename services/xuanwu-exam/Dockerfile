FROM micromoving/xuanwu/zulu-openjdk

WORKDIR /xuanwu

ARG JAR_FILE=target/xuanwu-exam-*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 9090

ENV TZ=Asia/Shanghai

ENTRYPOINT ["java", "-Dspring.config.additional-location=/xuanwu/config.yaml", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]