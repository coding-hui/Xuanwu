package top.wecoding.xuanwu.iam.client.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author wecoding
 * @since 0.8
 */
@Getter
@Setter
public class HttpBearerAuth implements Authentication {

	public static final String NAME = "bearer";

	public static final String DEFAULT_SCHEME = "bearer";

	private final String scheme;

	private String bearerToken;

	public HttpBearerAuth() {
		this(DEFAULT_SCHEME);
	}

	public HttpBearerAuth(String scheme) {
		this.scheme = scheme;
	}

	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
			throws IOException {
		HttpHeaders headers = request.getHeaders();
		if (bearerToken != null) {
			headers.set(AUTHORIZATION, (scheme != null ? upperCaseBearer(scheme) + " " : "") + bearerToken);
		}
		return execution.execute(request, body);
	}

	private String upperCaseBearer(String scheme) {
		return ("bearer".equalsIgnoreCase(scheme)) ? "Bearer" : scheme;
	}

}
