package top.wecoding.xuanwu.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.info.GitProperties;
import org.springframework.boot.jackson.autoconfigure.JacksonAutoConfiguration;
import org.springframework.boot.webmvc.autoconfigure.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import top.wecoding.xuanwu.core.exception.BaseExceptionHandler;
import top.wecoding.xuanwu.core.helper.ApplicationContextHelper;
import top.wecoding.xuanwu.core.jackson.CustomJavaTimeModule;
import top.wecoding.xuanwu.core.version.VersionController;

import java.util.Optional;

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

    /**
     * 注册自定义 的 jackson 时间格式，高优先级，用于覆盖默认的时间格式
     *
     * @return CustomJavaTimeModule
     */
    @Bean
    @ConditionalOnMissingBean(CustomJavaTimeModule.class)
    public CustomJavaTimeModule customJavaTimeModule() {
      return new CustomJavaTimeModule();
    }
  }

  @AutoConfiguration
  @ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.ANY)
  public static class CustomWebMvcAutoConfiguration implements WebMvcConfigurer {

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
      configurer.addPathPrefix("/{version}", _ -> true);
    }
  }
}
