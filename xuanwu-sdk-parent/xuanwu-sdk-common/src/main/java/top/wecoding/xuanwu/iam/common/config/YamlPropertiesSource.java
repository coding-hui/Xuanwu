package top.wecoding.xuanwu.iam.common.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import top.wecoding.xuanwu.iam.common.io.Resource;

/**
 * @author wecoding
 * @since 0.8
 */
@Slf4j
public class YamlPropertiesSource implements PropertiesSource {

  private final Resource resource;

  public YamlPropertiesSource(Resource resource) {
    Assert.notNull(resource, "resource argument cannot be null.");
    this.resource = resource;
  }

  @Override
  public Map<String, String> getProperties() {
    try (InputStream in = resource.getInputStream()) {
      // check to see if file exists
      if (in != null) { // if we have a yaml file.
        Yaml yaml = new Yaml(new SafeConstructor(new LoaderOptions()));
        return getFlattenedMap(yaml.load(in));
      }
    } catch (IOException e) {
      throw new IllegalArgumentException(
          "Unable to read resource [" + resource + "]: " + e.getMessage(), e);
    }
    return new LinkedHashMap<>();
  }

  private Map<String, String> getFlattenedMap(Map<String, Object> source) {
    Map<String, String> result = new LinkedHashMap<>();
    buildFlattenedMap(result, source, null);
    return result;
  }

  private void buildFlattenedMap(
      Map<String, String> result, Map<String, Object> source, String path) {
    for (Map.Entry<String, Object> entry : source.entrySet()) {
      String key = entry.getKey();
      if (StringUtils.hasText(path)) {
        if (key.startsWith("[")) {
          key = path + key;
        } else {
          key = path + "." + key;
        }
      }
      Object value = entry.getValue();
      if (value instanceof String) {
        result.put(key, String.valueOf(value));
      } else if (value instanceof Map) {
        // Need a compound key
        @SuppressWarnings("unchecked")
        Map<String, Object> map = (Map<String, Object>) value;
        buildFlattenedMap(result, map, key);
      } else if (value instanceof Collection) {
        // Need a compound key
        @SuppressWarnings("unchecked")
        Collection<Object> collection = (Collection<Object>) value;
        result.put(key, StringUtils.collectionToCommaDelimitedString(collection));
      } else {
        result.put(key, value != null ? String.valueOf(value) : "");
      }
    }
  }
}
