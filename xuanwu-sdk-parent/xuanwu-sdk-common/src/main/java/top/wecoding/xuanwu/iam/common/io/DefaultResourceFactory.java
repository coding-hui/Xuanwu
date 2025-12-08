package top.wecoding.xuanwu.iam.common.io;

import java.util.Locale;
import org.springframework.util.Assert;

/**
 * @author wecoding
 * @since 0.8
 */
public class DefaultResourceFactory implements ResourceFactory {

  @Override
  public Resource createResource(String location) {
    Assert.hasText(location, "location argument cannot be null or empty.");

    if (location.startsWith(ClasspathResource.SCHEME_PREFIX)) {
      return new ClasspathResource(location);
    }

    String lcase = location.toLowerCase(Locale.ROOT);

    if (location.startsWith(UrlResource.SCHEME_PREFIX)
        || lcase.startsWith("http:")
        || lcase.startsWith("https:")) {
      return new UrlResource(location);
    }

    return new FileResource(location);
  }
}
