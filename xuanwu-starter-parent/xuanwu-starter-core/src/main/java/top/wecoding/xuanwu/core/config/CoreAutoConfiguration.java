package top.wecoding.xuanwu.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;
import top.wecoding.xuanwu.core.exception.BaseExceptionHandler;
import top.wecoding.xuanwu.core.helper.ApplicationContextHelper;
import top.wecoding.xuanwu.core.jackson.CustomJavaTimeModule;

import java.time.ZoneId;
import java.util.Locale;
import java.util.TimeZone;

import static top.wecoding.xuanwu.core.constant.Constant.NORM_DATETIME_PATTERN;

/**
 * @author liuyuhui
 * @since 0.8
 */
@AutoConfiguration
public class CoreAutoConfiguration {

	@Bean
	public ApplicationContextHelper applicationContextHelper() {
		return new ApplicationContextHelper();
	}

	@Bean
	public BaseExceptionHandler baseExceptionHandler() {
		return new BaseExceptionHandler();
	}

	@ConditionalOnClass(ObjectMapper.class)
	@AutoConfiguration(before = JacksonAutoConfiguration.class)
	public static class CustomJacksonAutoConfiguration {

		/** Json 自定义序列化工具 Long 转 String, 防止 Long 传到前端存在精度问题 */
		@Bean
		@ConditionalOnMissingBean
		public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
			return builder -> builder.locale(Locale.CHINA)
				.timeZone(TimeZone.getTimeZone(ZoneId.systemDefault()))
				.simpleDateFormat(NORM_DATETIME_PATTERN)
				.serializerByType(Long.class, ToStringSerializer.instance)
				.serializerByType(Long.TYPE, ToStringSerializer.instance)
				.modules(new CustomJavaTimeModule());
		}

		/**
		 * 注册自定义 的 jackson 时间格式，高优先级，用于覆盖默认的时间格式
		 * @return CustomJavaTimeModule
		 */
		@Bean
		@ConditionalOnMissingBean(CustomJavaTimeModule.class)
		public CustomJavaTimeModule customJavaTimeModule() {
			return new CustomJavaTimeModule();
		}

	}

}
