package top.wecoding.xuanwu.iam.authc;

/**
 * @author wecoding
 * @since 0.8
 */
public class ApiTokenClientCredentials implements ClientCredentials<String> {

  private final String secret;

  public ApiTokenClientCredentials(String secret) {
    this.secret = secret;
  }

  @Override
  public String getCredentials() {
    return secret;
  }

  @Override
  public String toString() {
    return "<ApiTokenClientCredentials>";
  }
}
