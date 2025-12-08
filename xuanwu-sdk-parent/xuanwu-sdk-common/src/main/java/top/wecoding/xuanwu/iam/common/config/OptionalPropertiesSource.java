package top.wecoding.xuanwu.iam.common.config;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

/**
 * @author wecoding
 * @since 0.8
 */
@Slf4j
public class OptionalPropertiesSource implements PropertiesSource {

  private final PropertiesSource propertiesSource;

  public OptionalPropertiesSource(PropertiesSource source) {
    Assert.notNull(source, "source cannot be null.");
    this.propertiesSource = source;
  }

  @Override
  public Map<String, String> getProperties() {
    try {
      return propertiesSource.getProperties();
    } catch (Exception e) {
      log.debug("Unable to obtain properties from optional properties source {}", propertiesSource);
    }
    return new LinkedHashMap<>();
  }
}
