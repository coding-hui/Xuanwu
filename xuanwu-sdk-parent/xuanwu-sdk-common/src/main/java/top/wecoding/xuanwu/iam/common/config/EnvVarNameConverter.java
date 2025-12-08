package top.wecoding.xuanwu.iam.common.config;

/**
 * @author wecoding
 * @since 0.8
 */
public interface EnvVarNameConverter {

  String toEnvVarName(String dottedPropertyName);

  String toDottedPropertyName(String envVarName);
}
