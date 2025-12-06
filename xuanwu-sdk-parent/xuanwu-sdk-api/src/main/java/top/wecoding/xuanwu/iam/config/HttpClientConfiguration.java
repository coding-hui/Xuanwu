package top.wecoding.xuanwu.iam.config;

import lombok.Getter;
import lombok.Setter;
import top.wecoding.xuanwu.iam.client.Proxy;

/**
 * @author Wecoding Liu
 * @since 0.8
 */
@Getter
@Setter
public class HttpClientConfiguration {

    private long connectionTimeout;

    private int retryMaxElapsed;

    private int retryMaxAttempts;

    private String apiBase;

    private int proxyPort;

    private String proxyHost;

    private String proxyUsername;

    private String proxyPassword;

    private Proxy proxy;

    public Proxy getProxy() {
        if (this.proxy != null) {
            return proxy;
        }

        Proxy proxy = null;
        // use proxy overrides if they're set
        if ((getProxyPort() > 0 || getProxyHost() != null)
                && (getProxyUsername() == null || getProxyPassword() == null)) {
            proxy = new Proxy(getProxyHost(), getProxyPort());
        }
        else if (getProxyUsername() != null && getProxyPassword() != null) {
            proxy = new Proxy(getProxyHost(), getProxyPort(), getProxyUsername(), getProxyPassword());
        }

        this.proxy = proxy;
        return this.proxy;
    }

}
