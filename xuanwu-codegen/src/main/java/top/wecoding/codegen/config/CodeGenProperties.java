package top.wecoding.codegen.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wecoding
 * @since 0.9
 */
@ConfigurationProperties(prefix = CodeGenProperties.PREFIX)
public class CodeGenProperties {

	public static final String PREFIX = "xuanwu.codegen";

}
