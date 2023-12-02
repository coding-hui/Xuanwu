package top.wecoding.xuanwu.iam.common.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static top.wecoding.xuanwu.iam.common.constant.Configs.DEFAULT_CLIENT_CACHE_TTI_PROPERTY_NAME;
import static top.wecoding.xuanwu.iam.common.constant.Configs.DEFAULT_CLIENT_CACHE_TTL_PROPERTY_NAME;
import static top.wecoding.xuanwu.iam.common.constant.Configs.DEFAULT_CLIENT_CONNECTION_TIMEOUT_PROPERTY_NAME;
import static top.wecoding.xuanwu.iam.common.constant.Configs.DEFAULT_CLIENT_ORG_URL_PROPERTY_NAME;
import static top.wecoding.xuanwu.iam.common.constant.Configs.DEFAULT_CLIENT_REQUEST_TIMEOUT_PROPERTY_NAME;
import static top.wecoding.xuanwu.iam.common.constant.Configs.DEFAULT_CLIENT_RETRY_MAX_ATTEMPTS_PROPERTY_NAME;

/**
 * @author wecoding
 * @since 0.8
 */
public class DefaultEnvVarNameConverter implements EnvVarNameConverter {

	private final Map<String, String> envToDotPropMap;

	public DefaultEnvVarNameConverter() {
		this.envToDotPropMap = buildReverseLookupToMap(DEFAULT_CLIENT_ORG_URL_PROPERTY_NAME,
				DEFAULT_CLIENT_CONNECTION_TIMEOUT_PROPERTY_NAME, DEFAULT_CLIENT_REQUEST_TIMEOUT_PROPERTY_NAME,
				DEFAULT_CLIENT_RETRY_MAX_ATTEMPTS_PROPERTY_NAME, DEFAULT_CLIENT_CACHE_TTL_PROPERTY_NAME,
				DEFAULT_CLIENT_CACHE_TTI_PROPERTY_NAME);
	}

	private Map<String, String> buildReverseLookupToMap(String... dottedPropertyNames) {
		return Arrays.stream(dottedPropertyNames)
			.collect(Collectors.toMap(this::toEnvVarName, dottedPropertyName -> dottedPropertyName));
	}

	@Override
	public String toEnvVarName(String dottedPropertyName) {
		Assert.hasText(dottedPropertyName, "dottedPropertyName argument cannot be null or empty.");
		dottedPropertyName = StringUtils.trim(dottedPropertyName);

		StringBuilder sb = new StringBuilder();

		for (char c : dottedPropertyName.toCharArray()) {
			if (c == '.') {
				sb.append('_');
				continue;
			}
			sb.append(Character.toUpperCase(c));
		}

		return sb.toString();
	}

	@Override
	public String toDottedPropertyName(String envVarName) {
		Assert.hasText(envVarName, "envVarName argument cannot be null or empty.");
		envVarName = StringUtils.trim(envVarName);

		// special cases (camel case):
		if (envToDotPropMap.containsKey(envVarName)) {
			return envToDotPropMap.get(envVarName);
		}

		// default cases:
		StringBuilder sb = new StringBuilder();

		for (char c : envVarName.toCharArray()) {
			if (c == '_') {
				sb.append('.');
				continue;
			}
			sb.append(Character.toLowerCase(c));
		}

		return sb.toString();
	}

}
