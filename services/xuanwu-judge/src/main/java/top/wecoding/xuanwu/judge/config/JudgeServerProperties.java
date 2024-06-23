package top.wecoding.xuanwu.judge.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author wecoding
 * @since 0.9
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = JudgeServerProperties.PREFIX)
public class JudgeServerProperties {

	public static final String PREFIX = "xuanwu.judge";

	private Integer workers;

}
