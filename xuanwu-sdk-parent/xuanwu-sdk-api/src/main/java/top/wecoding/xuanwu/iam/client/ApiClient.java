package top.wecoding.xuanwu.iam.client;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.support.RestTemplateAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import org.springframework.web.util.DefaultUriBuilderFactory;
import top.wecoding.xuanwu.iam.api.AuthenticationApi;
import top.wecoding.xuanwu.iam.api.UserApi;
import top.wecoding.xuanwu.iam.client.interceptor.BearerTokenAuthenticationInterceptor;
import top.wecoding.xuanwu.iam.config.ClientConfiguration;

/**
 * @author wecoding
 * @since 0.8
 */
public class ApiClient {

  private static final String DEFAULT_API_BASE = "http://iam.wecoding.top";

  private final RestTemplate restTemplate;

  private final HttpServiceProxyFactory factory;

  public ApiClient(ClientConfiguration clientConfig, RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
    this.setBasePath(clientConfig.getApiBase());

    RestTemplateAdapter adapter = RestTemplateAdapter.create(restTemplate);
    this.factory = HttpServiceProxyFactory.builderFor(adapter).build();
  }

  public ApiClient setBasePath(String basePath) {
    basePath = StringUtils.isBlank(basePath) ? DEFAULT_API_BASE : basePath;
    this.restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(basePath));
    return this;
  }

  public ApiClient setApiToken(String apiToken) {
    List<ClientHttpRequestInterceptor> interceptors =
        this.restTemplate.getInterceptors().stream()
            .filter(i -> !(i instanceof BearerTokenAuthenticationInterceptor))
            .collect(Collectors.toList());
    interceptors.add(new BearerTokenAuthenticationInterceptor(apiToken));
    this.restTemplate.setInterceptors(interceptors);
    return this;
  }

  public AuthenticationApi authenticationApi() {
    return this.factory.createClient(AuthenticationApi.class);
  }

  public UserApi userApi() {
    return this.factory.createClient(UserApi.class);
  }
}
