package top.wecoding.xuanwu.codegen.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import top.wecoding.xuanwu.iam.api.AuthenticationApi;
import top.wecoding.xuanwu.iam.client.ApiClient;
import top.wecoding.xuanwu.iam.client.Clients;

/**
 * @author wecoding
 * @since 0.8
 */
@Configuration
public class IamClientConfig {

  @Bean
  public ApiClient apiClient() {
    return Clients.builder().build();
  }

  @Bean
  public AuthenticationApi authenticationApi(ApiClient apiClient) {
    return apiClient.authenticationApi();
  }
}
