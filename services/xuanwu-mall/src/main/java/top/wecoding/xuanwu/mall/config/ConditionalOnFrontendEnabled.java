package top.wecoding.xuanwu.mall.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author wecoding
 * @since 0.8
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ConditionalOnProperty(value = "xuanwu.mall.frontend.enabled", havingValue = "true", matchIfMissing = true)
public @interface ConditionalOnFrontendEnabled {

}
