package top.wecoding.xuanwu.iam.client;

import top.wecoding.xuanwu.iam.authc.ClientCredentials;

/**
 * @author Wecoding Liu
 * @since 0.8
 */
@SuppressWarnings("rawtypes")
public interface ClientBuilder {

    ClientBuilder setApiBase(String apiBase);

    ClientBuilder setConnectionTimeout(long connectionTimeout);

    ClientBuilder setClientCredentials(ClientCredentials clientCredentials);

    ClientBuilder setProxy(Proxy proxy);

    ClientBuilder setRetryMaxElapsed(int maxElapsed);

    ClientBuilder setRetryMaxAttempts(int maxAttempts);

    ApiClient build();

}
