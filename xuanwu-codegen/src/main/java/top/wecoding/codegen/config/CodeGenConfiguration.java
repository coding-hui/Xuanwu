package top.wecoding.codegen.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author wecoding
 * @since 0.9
 */
@Configuration
@EnableConfigurationProperties(CodeGenProperties.class)
public class CodeGenConfiguration {

}
