package top.wecoding.xuanwu.codegen.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * @author wecoding
 * @since 0.8
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ConditionalOnProperty(value = "xuanwu.codegen.frontend.enabled", havingValue = "true")
public @interface ConditionalOnFrontendEnabled {}
