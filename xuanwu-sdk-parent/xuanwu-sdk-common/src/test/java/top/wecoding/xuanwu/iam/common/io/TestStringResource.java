package top.wecoding.xuanwu.iam.common.io;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author wecoding
 * @since 0.8
 */
public class TestStringResource implements Resource {

  private String string;

  public TestStringResource(String string) {
    this.string = string;
  }

  @Override
  public InputStream getInputStream() throws IOException {
    return new ByteArrayInputStream(string.getBytes());
  }
}
