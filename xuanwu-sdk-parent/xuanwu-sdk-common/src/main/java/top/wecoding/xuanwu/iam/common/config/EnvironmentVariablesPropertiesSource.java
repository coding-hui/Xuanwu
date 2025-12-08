package top.wecoding.xuanwu.iam.common.config;

import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.util.CollectionUtils;

/**
 * @author wecoding
 * @since 0.8
 */
public class EnvironmentVariablesPropertiesSource implements PropertiesSource {

  public static PropertiesSource filteredPropertiesSource() {
    return new FilteredEnvironmentPropertiesSource();
  }

  @Override
  public Map<String, String> getProperties() {

    Map<String, String> envVars = System.getenv();

    if (!CollectionUtils.isEmpty(envVars)) {
      return new LinkedHashMap<>(envVars);
    }

    return java.util.Collections.emptyMap();
  }

  private static class FilteredEnvironmentPropertiesSource extends FilteredPropertiesSource {

    private static final EnvVarNameConverter ENV_VAR_NAME_CONVERTER =
        new DefaultEnvVarNameConverter();

    private FilteredEnvironmentPropertiesSource() {
      super(
          new EnvironmentVariablesPropertiesSource(),
          (key, value) -> {
            if (key.startsWith("WECODING_")) {
              // we want to convert env var naming convention to dotted property
              // convention
              // to allow overrides. Overrides work based on overriding
              // identically-named keys:
              key = ENV_VAR_NAME_CONVERTER.toDottedPropertyName(key);
              return new String[] {key, value};
            } else {
              return null;
            }
          });
    }
  }
}
