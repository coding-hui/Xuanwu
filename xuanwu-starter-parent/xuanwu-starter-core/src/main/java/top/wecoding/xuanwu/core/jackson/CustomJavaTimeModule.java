package top.wecoding.xuanwu.core.jackson;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static top.wecoding.xuanwu.core.constant.Constant.NORM_DATETIME_PATTERN;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.PackageVersion;
import com.fasterxml.jackson.datatype.jsr310.deser.InstantDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.InstantSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

/**
 * 时间序列化，自定义java8新增时间类型的序列化
 *
 * @author liuyuhui
 * @date 2022/04/17
 * @see com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
 */
public class CustomJavaTimeModule extends SimpleModule {

  public CustomJavaTimeModule() {
    super(PackageVersion.VERSION);

    // yyyy-MM-dd HH:mm:ss
    this.addSerializer(
        LocalDateTime.class,
        new LocalDateTimeSerializer(DateTimeFormatter.ofPattern(NORM_DATETIME_PATTERN)));
    // yyyy-MM-dd
    this.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_LOCAL_DATE));
    // HH:mm:ss
    this.addSerializer(LocalTime.class, new LocalTimeSerializer(DateTimeFormatter.ISO_LOCAL_TIME));
    // INSTANCE
    this.addSerializer(Instant.class, InstantSerializer.INSTANCE);

    // yyyy-MM-dd HH:mm:ss
    this.addDeserializer(
        LocalDateTime.class,
        new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(NORM_DATETIME_PATTERN)));
    // yyyy-MM-dd
    this.addDeserializer(
        LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_LOCAL_DATE));
    // HH:mm:ss
    this.addDeserializer(
        LocalTime.class, new LocalTimeDeserializer(DateTimeFormatter.ISO_LOCAL_TIME));
    // INSTANT
    this.addDeserializer(Instant.class, InstantDeserializer.INSTANT);
  }
}
