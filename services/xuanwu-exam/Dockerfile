FROM micromoving/xuanwu/zulu-openjdk:25.0.1-jre

WORKDIR /xuanwu

ARG JAR_FILE=target/xuanwu-exam-*.jar

COPY ${JAR_FILE} app.jar

EXPOSE 6789

ENV TZ=Asia/Shanghai

ENTRYPOINT ["java"]

CMD ["-Dspring.config.additional-location=/xuanwu/config.yaml", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
