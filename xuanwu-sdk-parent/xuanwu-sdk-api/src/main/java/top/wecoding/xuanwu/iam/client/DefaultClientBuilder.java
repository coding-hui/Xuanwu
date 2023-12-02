package top.wecoding.xuanwu.iam.client;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.config.ConnectionConfig;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultAuthenticationStrategy;
import org.apache.hc.client5.http.impl.DefaultConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.impl.DefaultHttpRequestRetryStrategy;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.DefaultBackoffStrategy;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.impl.DefaultConnectionReuseStrategy;
import org.apache.hc.core5.util.Timeout;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import top.wecoding.xuanwu.iam.authc.ClientCredentials;
import top.wecoding.xuanwu.iam.authc.ClientCredentialsProvider;
import top.wecoding.xuanwu.iam.authc.DefaultClientCredentialsProvider;
import top.wecoding.xuanwu.iam.common.config.EnvironmentVariablesPropertiesSource;
import top.wecoding.xuanwu.iam.common.config.OptionalPropertiesSource;
import top.wecoding.xuanwu.iam.common.config.PropertiesSource;
import top.wecoding.xuanwu.iam.common.config.ResourcePropertiesSource;
import top.wecoding.xuanwu.iam.common.config.SystemPropertiesSource;
import top.wecoding.xuanwu.iam.common.config.YamlPropertiesSource;
import top.wecoding.xuanwu.iam.common.io.ClasspathResource;
import top.wecoding.xuanwu.iam.common.io.DefaultResourceFactory;
import top.wecoding.xuanwu.iam.common.io.Resource;
import top.wecoding.xuanwu.iam.common.io.ResourceFactory;
import top.wecoding.xuanwu.iam.common.util.Strings;
import top.wecoding.xuanwu.iam.config.ClientConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import static top.wecoding.xuanwu.iam.common.constant.Configs.DEFAULT_CLIENT_API_TOKEN_PROPERTY_NAME;
import static top.wecoding.xuanwu.iam.common.constant.Configs.DEFAULT_CLIENT_CACHE_ENABLED_PROPERTY_NAME;
import static top.wecoding.xuanwu.iam.common.constant.Configs.DEFAULT_CLIENT_CACHE_TTI_PROPERTY_NAME;
import static top.wecoding.xuanwu.iam.common.constant.Configs.DEFAULT_CLIENT_CACHE_TTL_PROPERTY_NAME;
import static top.wecoding.xuanwu.iam.common.constant.Configs.DEFAULT_CLIENT_CONNECTION_TIMEOUT_PROPERTY_NAME;
import static top.wecoding.xuanwu.iam.common.constant.Configs.DEFAULT_CLIENT_ORG_URL_PROPERTY_NAME;
import static top.wecoding.xuanwu.iam.common.constant.Configs.DEFAULT_CLIENT_PROXY_HOST_PROPERTY_NAME;
import static top.wecoding.xuanwu.iam.common.constant.Configs.DEFAULT_CLIENT_PROXY_PASSWORD_PROPERTY_NAME;
import static top.wecoding.xuanwu.iam.common.constant.Configs.DEFAULT_CLIENT_PROXY_PORT_PROPERTY_NAME;
import static top.wecoding.xuanwu.iam.common.constant.Configs.DEFAULT_CLIENT_PROXY_USERNAME_PROPERTY_NAME;
import static top.wecoding.xuanwu.iam.common.constant.Configs.DEFAULT_CLIENT_REQUEST_TIMEOUT_PROPERTY_NAME;
import static top.wecoding.xuanwu.iam.common.constant.Configs.DEFAULT_CLIENT_RETRY_MAX_ATTEMPTS_PROPERTY_NAME;

/**
 * @author wecoding
 * @since 0.8
 */
@Slf4j
@SuppressWarnings("rawtypes")
public class DefaultClientBuilder implements ClientBuilder {

	private static final String ENVVARS_TOKEN = "envvars";

	private static final String SYSPROPS_TOKEN = "sysprops";

	private static final String WECODING_SDK_CONFIG = "top.wecoding.xuanwu.iam/config/";

	private static final String WECODING_YAML = "wecoding.yaml";

	private static final String WECODING_PROPERTIES = "wecoding.properties";

	@Getter
	private final ClientConfiguration clientConfig = new ClientConfiguration();

	private ClientCredentials clientCredentials;

	public DefaultClientBuilder() {
		this(new DefaultResourceFactory());
	}

	DefaultClientBuilder(ResourceFactory resourceFactory) {
		Collection<PropertiesSource> sources = new ArrayList<>();

		for (String location : configSources()) {

			if (ENVVARS_TOKEN.equalsIgnoreCase(location)) {
				sources.add(EnvironmentVariablesPropertiesSource.filteredPropertiesSource());
			}
			else if (SYSPROPS_TOKEN.equalsIgnoreCase(location)) {
				sources.add(SystemPropertiesSource.filteredPropertiesSource());
			}
			else {
				Resource resource = resourceFactory.createResource(location);

				PropertiesSource wrappedSource;
				if (Strings.endsWithIgnoreCase(location, ".yaml") || Strings.endsWithIgnoreCase(location, ".yml")) {
					wrappedSource = new YamlPropertiesSource(resource);
				}
				else {
					wrappedSource = new ResourcePropertiesSource(resource);
				}

				PropertiesSource propertiesSource = new OptionalPropertiesSource(wrappedSource);
				sources.add(propertiesSource);
			}
		}

		Map<String, String> props = new LinkedHashMap<>();

		for (PropertiesSource source : sources) {
			Map<String, String> srcProps = source.getProperties();
			props.putAll(srcProps);
		}

		// check to see if property value is null before setting value
		// if != null, allow it to override previously set values

		// @formatter:off
		if (Strings.hasText(props.get(DEFAULT_CLIENT_API_TOKEN_PROPERTY_NAME))) {
			clientConfig.setApiToken(props.get(DEFAULT_CLIENT_API_TOKEN_PROPERTY_NAME));
		}

		if (Strings.hasText(props.get(DEFAULT_CLIENT_CACHE_ENABLED_PROPERTY_NAME))) {
			clientConfig.setCacheManagerEnabled(Boolean.parseBoolean(props.get(DEFAULT_CLIENT_CACHE_ENABLED_PROPERTY_NAME)));
		}

		if (Strings.hasText(props.get(DEFAULT_CLIENT_CACHE_TTL_PROPERTY_NAME))) {
			clientConfig.setCacheManagerTtl(Long.parseLong(props.get(DEFAULT_CLIENT_CACHE_TTL_PROPERTY_NAME)));
		}

		if (Strings.hasText(props.get(DEFAULT_CLIENT_CACHE_TTI_PROPERTY_NAME))) {
			clientConfig.setCacheManagerTti(Long.parseLong(props.get(DEFAULT_CLIENT_CACHE_TTI_PROPERTY_NAME)));
		}

		if (Strings.hasText(props.get(DEFAULT_CLIENT_ORG_URL_PROPERTY_NAME))) {
			String baseUrl = props.get(DEFAULT_CLIENT_ORG_URL_PROPERTY_NAME);
			// remove backslashes that can end up in file when it's written programmatically, e.g. in a test
			baseUrl = baseUrl.replace("\\:", ":");
			clientConfig.setBaseUrl(baseUrl);
		}

		if (Strings.hasText(props.get(DEFAULT_CLIENT_CONNECTION_TIMEOUT_PROPERTY_NAME))) {
			clientConfig.setConnectionTimeout(Integer.parseInt(props.get(DEFAULT_CLIENT_CONNECTION_TIMEOUT_PROPERTY_NAME)));
		}

		if (Strings.hasText(props.get(DEFAULT_CLIENT_PROXY_PORT_PROPERTY_NAME))) {
			clientConfig.setProxyPort(Integer.parseInt(props.get(DEFAULT_CLIENT_PROXY_PORT_PROPERTY_NAME)));
		}

		if (Strings.hasText(props.get(DEFAULT_CLIENT_PROXY_HOST_PROPERTY_NAME))) {
			clientConfig.setProxyHost(props.get(DEFAULT_CLIENT_PROXY_HOST_PROPERTY_NAME));
		}

		if (Strings.hasText(props.get(DEFAULT_CLIENT_PROXY_USERNAME_PROPERTY_NAME))) {
			clientConfig.setProxyUsername(props.get(DEFAULT_CLIENT_PROXY_USERNAME_PROPERTY_NAME));
		}

		if (Strings.hasText(props.get(DEFAULT_CLIENT_PROXY_PASSWORD_PROPERTY_NAME))) {
			clientConfig.setProxyPassword(props.get(DEFAULT_CLIENT_PROXY_PASSWORD_PROPERTY_NAME));
		}

		if (Strings.hasText(props.get(DEFAULT_CLIENT_REQUEST_TIMEOUT_PROPERTY_NAME))) {
			clientConfig.setRetryMaxElapsed(Integer.parseInt(props.get(DEFAULT_CLIENT_REQUEST_TIMEOUT_PROPERTY_NAME)));
		}

		if (Strings.hasText(props.get(DEFAULT_CLIENT_RETRY_MAX_ATTEMPTS_PROPERTY_NAME))) {
			clientConfig.setRetryMaxAttempts(Integer.parseInt(props.get(DEFAULT_CLIENT_RETRY_MAX_ATTEMPTS_PROPERTY_NAME)));
		}
		// @formatter:on
	}

	private static String[] configSources() {
		// lazy load the config sources as the user.home system prop could change for
		// testing
		// @formatter:off
		return new String[] {
				ClasspathResource.SCHEME_PREFIX + WECODING_SDK_CONFIG + WECODING_PROPERTIES,
				ClasspathResource.SCHEME_PREFIX + WECODING_SDK_CONFIG + WECODING_YAML,
				ClasspathResource.SCHEME_PREFIX + WECODING_PROPERTIES,
				ClasspathResource.SCHEME_PREFIX + WECODING_YAML,
				System.getProperty("user.home") + File.separatorChar + ".wecoding" + File.separatorChar + WECODING_YAML,
				ENVVARS_TOKEN,
				SYSPROPS_TOKEN
		};
		// @formatter:on
	}

	@Override
	public ClientBuilder setApiBase(String apiBase) {
		this.clientConfig.setBaseUrl(apiBase);
		return this;
	}

	@Override
	public ClientBuilder setConnectionTimeout(long connectionTimeout) {
		Assert.isTrue(connectionTimeout >= 0, "Timeout cannot be a negative number.");
		this.clientConfig.setConnectionTimeout(connectionTimeout);
		return this;
	}

	@Override
	public ClientBuilder setClientCredentials(ClientCredentials clientCredentials) {
		Assert.isInstanceOf(ClientCredentials.class, clientCredentials);
		this.clientCredentials = clientCredentials;
		return this;
	}

	@Override
	public ClientBuilder setProxy(Proxy proxy) {
		if (proxy == null) {
			throw new IllegalArgumentException("proxy argument cannot be null.");
		}
		else {
			this.clientConfig.setProxyHost(proxy.getHost());
			this.clientConfig.setProxyPort(proxy.getPort());
			this.clientConfig.setProxyUsername(proxy.getUsername());
			this.clientConfig.setProxyPassword(proxy.getPassword());
			return this;
		}
	}

	@Override
	public ClientBuilder setRetryMaxElapsed(int maxElapsed) {
		this.clientConfig.setRetryMaxElapsed(maxElapsed);
		return this;
	}

	@Override
	public ClientBuilder setRetryMaxAttempts(int maxAttempts) {
		this.clientConfig.setRetryMaxAttempts(maxAttempts);
		return this;
	}

	@Override
	public ApiClient build() {
		// @formatter:off
		HttpClientBuilder httpClientBuilder = createHttpClientBuilder(clientConfig);

		if (this.clientConfig.getProxy() != null) {
			this.setProxy(httpClientBuilder, this.clientConfig);
		}

		RestTemplate restTemplate = new RestTemplate(Arrays.asList(
				new MappingJackson2HttpMessageConverter(),
				new FormHttpMessageConverter(),
				new StringHttpMessageConverter())
		);

		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClientBuilder.build()));

		Optional<BasicAuthenticationInterceptor> basicAuthenticationInterceptor = Optional.empty();
		if (StringUtils.isNotBlank(clientConfig.getProxyHost())) {
			String username = clientConfig.getProxyUsername();
			String password = clientConfig.getProxyPassword();
			if (!StringUtils.isAnyBlank(username, password)) {
				basicAuthenticationInterceptor = Optional.of(new BasicAuthenticationInterceptor(username, password));
			}
		}
		basicAuthenticationInterceptor.ifPresent(restTemplate.getInterceptors()::add);

		ApiClient apiClient = new ApiClient(clientConfig, restTemplate);

		apiClient.setBasePath(this.clientConfig.getBaseUrl());

		ClientCredentialsProvider clientCredentialsProvider = this.clientConfig.getClientCredentialsProvider();
		if (clientCredentialsProvider == null && this.clientCredentials != null) {
			this.clientConfig.setClientCredentialsProvider(new DefaultClientCredentialsProvider(this.clientCredentials));
		}
		else if (clientCredentialsProvider == null) {
			this.clientConfig.setClientCredentialsProvider(new DefaultClientCredentialsProvider(this.clientConfig));
		}

		Object apiToken = this.clientConfig.getClientCredentialsProvider().resolveCredentials().getCredentials();

		apiClient.setApiToken((String) apiToken);

		// @formatter:on
		return apiClient;
	}

	/**
	 * Override to customize the client, allowing one to add additional interceptors.
	 * @param clientConfig the current ClientConfiguration
	 * @return an {@link HttpClientBuilder} initialized with default configuration
	 */
	protected HttpClientBuilder createHttpClientBuilder(ClientConfiguration clientConfig) {
		return HttpClients.custom()
			.setDefaultRequestConfig(createHttpRequestConfigBuilder(clientConfig).build())
			.setConnectionManager(createHttpClientConnectionManagerBuilder(clientConfig).build())
			.setRetryStrategy(new DefaultHttpRequestRetryStrategy())
			.setConnectionBackoffStrategy(new DefaultBackoffStrategy())
			.setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
			.setConnectionReuseStrategy(new DefaultConnectionReuseStrategy())
			.disableCookieManagement();
	}

	/**
	 * Override to customize the request config
	 * @param clientConfig the current clientConfig
	 * @return a {@link RequestConfig.Builder} initialized with default configuration
	 */
	protected RequestConfig.Builder createHttpRequestConfigBuilder(ClientConfiguration clientConfig) {
		return RequestConfig.custom()
			.setResponseTimeout(Timeout.ofSeconds(clientConfig.getConnectionTimeout()))
			.setConnectionRequestTimeout(Timeout.ofSeconds(clientConfig.getConnectionTimeout()));
	}

	/**
	 * Override to customize the connection manager, allowing the increase of max
	 * connections
	 * @param clientConfig the current clientConfig
	 * @return a {@link PoolingHttpClientConnectionManagerBuilder} initialized with
	 * default configuration
	 */
	protected PoolingHttpClientConnectionManagerBuilder createHttpClientConnectionManagerBuilder(
			ClientConfiguration clientConfig) {
		return PoolingHttpClientConnectionManagerBuilder.create()
			.setDefaultConnectionConfig(ConnectionConfig.custom()
				.setConnectTimeout(Timeout.ofSeconds(clientConfig.getConnectionTimeout()))
				.build());
	}

	private void setProxy(HttpClientBuilder clientBuilder, ClientConfiguration clientConfig) {
		clientBuilder.useSystemProperties();
		clientBuilder.setProxy(new HttpHost(clientConfig.getProxyHost(), clientConfig.getProxyPort()));
		if (clientConfig.getProxyUsername() != null) {
			BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
			AuthScope authScope = new AuthScope(clientConfig.getProxyHost(), clientConfig.getProxyPort());
			UsernamePasswordCredentials usernamePasswordCredentials = new UsernamePasswordCredentials(
					clientConfig.getProxyUsername(), clientConfig.getProxyPassword().toCharArray());
			credentialsProvider.setCredentials(authScope, usernamePasswordCredentials);
			clientBuilder.setDefaultCredentialsProvider(credentialsProvider);
			clientBuilder.setProxyAuthenticationStrategy(new DefaultAuthenticationStrategy());
		}
	}

}
