package top.wecoding.xuanwu.iam.common.config;

import java.io.IOException;
import java.util.Map;
import org.springframework.util.Assert;
import top.wecoding.xuanwu.iam.common.io.Resource;

/**
 * @author wecoding
 * @since 0.8
 */
public class ResourcePropertiesSource implements PropertiesSource {

  private final Resource resource;

  public ResourcePropertiesSource(Resource resource) {
    Assert.notNull(resource, "resource argument cannot be null.");
    this.resource = resource;
  }

  @Override
  public Map<String, String> getProperties() {
    try {
      return new DefaultPropertiesParser().parse(resource);
    } catch (IOException e) {
      throw new IllegalArgumentException(
          "Unable to read resource [" + resource + "]: " + e.getMessage(), e);
    }
  }
}
