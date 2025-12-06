package top.wecoding.xuanwu.iam.config;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import top.wecoding.xuanwu.iam.authc.ClientCredentialsProvider;

/**
 * @author Wecoding Liu
 * @since 0.8
 */
@Getter
@Setter
public class ClientConfiguration extends HttpClientConfiguration {

    private String apiToken;

    private boolean cacheManagerEnabled;

    private long cacheManagerTtl;

    private long cacheManagerTti;

    private ClientCredentialsProvider clientCredentialsProvider;

    public String getApiBase() {
        String baseUrl = super.getApiBase();
        return StringUtils.isBlank(baseUrl) ? "/" : baseUrl;
    }

}