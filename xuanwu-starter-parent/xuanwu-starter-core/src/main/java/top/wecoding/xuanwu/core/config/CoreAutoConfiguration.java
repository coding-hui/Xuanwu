package top.wecoding.xuanwu.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import top.wecoding.xuanwu.core.annotation.Version;
import top.wecoding.xuanwu.core.exception.BaseExceptionHandler;
import top.wecoding.xuanwu.core.helper.ApplicationContextHelper;
import top.wecoding.xuanwu.core.jackson.CustomJavaTimeModule;
import top.wecoding.xuanwu.core.version.VersionController;

import java.lang.reflect.Method;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;
import java.util.TimeZone;

import static top.wecoding.xuanwu.core.constant.Constant.NORM_DATETIME_PATTERN;

/**
 * @author liuyuhui
 * @since 0.8
 */
@Slf4j
@AutoConfiguration
public class CoreAutoConfiguration implements WebMvcRegistrations {

	@Bean
	@ConditionalOnBean(GitProperties.class)
	public VersionController versionController(GitProperties gitProperties) {
		return new VersionController(Optional.of(gitProperties));
	}

	/**
	 * 根据 @Version 注解统一更新接口版本 -> /api/version/xxx
	 */
	@Override
	public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
		return new RequestMappingHandlerMapping() {
			@Override
			protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {
				Version version = Optional.ofNullable(AnnotatedElementUtils.findMergedAnnotation(method, Version.class))
					.orElse(AnnotatedElementUtils.findMergedAnnotation(handlerType, Version.class));
				if (version == null || ObjectUtils.isEmpty(version.value())) {
					return super.getMappingForMethod(method, handlerType);
				}
				try {
					RequestMappingInfo info = super.getMappingForMethod(method, handlerType);
					if (info == null) {
						return null;
					}
					String[] basePath = Arrays.stream(version.value())
						.map(v -> "/api" + (v.startsWith("/") ? v : "/" + v))
						.toArray(String[]::new);
					return RequestMappingInfo.paths(basePath).build().combine(info);
				}
				catch (Exception e) {
					return super.getMappingForMethod(method, handlerType);
				}
			}
		};
	}

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
