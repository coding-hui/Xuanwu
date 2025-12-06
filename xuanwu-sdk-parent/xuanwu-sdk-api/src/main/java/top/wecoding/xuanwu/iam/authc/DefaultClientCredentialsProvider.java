package top.wecoding.xuanwu.iam.authc;

import top.wecoding.xuanwu.iam.config.ClientConfiguration;

/**
 * @author wecoding
 * @since 0.8
 */
@SuppressWarnings("rawtypes")
public class DefaultClientCredentialsProvider implements ClientCredentialsProvider {

    private final ClientCredentials clientCredentials;

    public DefaultClientCredentialsProvider(ClientCredentials clientCredentials) {
        this.clientCredentials = clientCredentials;
    }

    public DefaultClientCredentialsProvider(ClientConfiguration clientConfiguration) {
        this(new ApiTokenClientCredentials(clientConfiguration.getApiToken()));
    }

    @Override
    public ClientCredentials resolveCredentials() {
        return this.clientCredentials;
    }

}
