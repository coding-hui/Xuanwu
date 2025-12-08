package top.wecoding.xuanwu.iam.common.io;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.springframework.util.Assert;

/**
 * @author wecoding
 * @since 0.8
 */
public class StringResource implements Resource {

  private static final Charset UTF_8 = StandardCharsets.UTF_8;

  private final String string;

  private final Charset charset;

  public StringResource(String s) {
    this(s, UTF_8);
  }

  public StringResource(String string, Charset charset) {
    Assert.hasText(string, "String argument cannot be null or empty.");
    Assert.notNull(charset, "Charset argument cannot be null or empty.");
    this.string = string;
    this.charset = charset;
  }

  @Override
  public InputStream getInputStream() {
    return new ByteArrayInputStream(string.getBytes(charset));
  }
}
