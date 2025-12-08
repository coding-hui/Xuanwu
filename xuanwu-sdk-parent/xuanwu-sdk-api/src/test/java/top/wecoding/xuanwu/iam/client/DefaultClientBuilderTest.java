package top.wecoding.xuanwu.iam.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;

import java.util.Properties;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import top.wecoding.xuanwu.iam.common.io.DefaultResourceFactory;
import top.wecoding.xuanwu.iam.common.io.Resource;
import top.wecoding.xuanwu.iam.common.io.ResourceFactory;
import top.wecoding.xuanwu.iam.config.ClientConfiguration;

/**
 * @author wecoding
 * @since 0.8
 */
class DefaultClientBuilderTest {

  private Properties originalProperties;

  static ResourceFactory noDefaultYamlResourceFactory() {
    val resourceFactory = spy(new DefaultResourceFactory());
    doAnswer(
            (Answer<Resource>)
                invocation -> {
                  if (invocation.getArgument(0).toString().endsWith("/.wecoding/wecoding.yaml")) {
                    return (Resource) () -> null;
                  } else {
                    return (Resource) invocation.callRealMethod();
                  }
                })
        .when(resourceFactory)
        .createResource(anyString());

    return resourceFactory;
  }

  @BeforeEach
  void setUp() {
    originalProperties = System.getProperties();
    System.setProperties(copyOf(originalProperties));
  }

  @AfterEach
  void tearDown() {
    System.setProperties(originalProperties);
  }

  @Test
  void testDefaultBuilder() {
    assertInstanceOf(DefaultClientBuilder.class, Clients.builder());
    val builder = new DefaultClientBuilder(noDefaultYamlResourceFactory());

    assertEquals("https://api.wecoding.top/v1", builder.getClientConfig().getApiBase());
    assertEquals(20, builder.getClientConfig().getConnectionTimeout());
  }

  @Test
  void testConfigureProxy() {
    DefaultClientBuilder clientBuilder = new DefaultClientBuilder();

    ClientConfiguration clientConfig = clientBuilder.getClientConfig();
    assertEquals("proxyhost", clientConfig.getProxyHost());
    assertEquals(8990, clientConfig.getProxyPort());
    assertEquals("proxyuser", clientConfig.getProxyUsername());
    assertEquals("pwd", clientConfig.getProxyPassword());

    Proxy proxy = clientConfig.getProxy();
    assertEquals("proxyhost", proxy.getHost());
    assertEquals(8990, proxy.getPort());
    assertEquals("proxyuser", proxy.getUsername());
    assertEquals("pwd", proxy.getPassword());
  }

  private Properties copyOf(Properties source) {
    Properties copy = new Properties();
    copy.putAll(source);
    return copy;
  }
}
