package top.wecoding.xuanwu.iam.common.config;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import lombok.val;
import org.springframework.util.StringUtils;

/**
 * @author wecoding
 * @since 0.8
 */
public class SystemPropertiesSource implements PropertiesSource {

  public static PropertiesSource filteredPropertiesSource() {
    return new FilteredSystemPropertiesSource();
  }

  @Override
  public Map<String, String> getProperties() {
    Map<String, String> properties = new LinkedHashMap<>();
    Properties systemProps = System.getProperties();

    if (systemProps != null && !systemProps.isEmpty()) {
      val e = systemProps.propertyNames();
      while (e.hasMoreElements()) {
        Object name = e.nextElement();
        String key = String.valueOf(name);
        String value = systemProps.getProperty(key);
        if (StringUtils.hasText(value)) {
          properties.put(key, value);
        }
      }
    }

    return properties;
  }

  private static class FilteredSystemPropertiesSource extends FilteredPropertiesSource {

    private FilteredSystemPropertiesSource() {
      super(
          new SystemPropertiesSource(),
          (key, value) -> {
            if (key.startsWith("okta.")) {
              return new String[] {key, value};
            }
            return null;
          });
    }
  }
}
