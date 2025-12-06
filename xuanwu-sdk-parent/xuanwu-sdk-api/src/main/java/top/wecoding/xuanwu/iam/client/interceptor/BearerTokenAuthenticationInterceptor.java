package top.wecoding.xuanwu.iam.client.interceptor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author wecoding
 * @since 0.8
 */
public class BearerTokenAuthenticationInterceptor implements ClientHttpRequestInterceptor {

	public static final String DEFAULT_SCHEME = "bearer";

	private final String scheme;

	private final String bearerToken;

	public BearerTokenAuthenticationInterceptor(String bearerToken) {
		this(DEFAULT_SCHEME, bearerToken);
	}

	public BearerTokenAuthenticationInterceptor(String scheme, String bearerToken) {
		this.scheme = scheme;
		this.bearerToken = bearerToken;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		HttpHeaders headers = request.getHeaders();
		if (bearerToken != null && !headers.containsHeader(HttpHeaders.AUTHORIZATION)) {
			headers.set(AUTHORIZATION, (scheme != null ? upperCaseBearer(scheme) + " " : "") + bearerToken);
		}
		return execution.execute(request, body);
	}

	private String upperCaseBearer(String scheme) {
		return ("bearer".equalsIgnoreCase(scheme)) ? "Bearer" : scheme;
	}

}
