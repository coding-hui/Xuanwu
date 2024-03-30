package top.wecoding.xuanwu.ai.minimax.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.function.Consumer;

/**
 * @author wecoding
 * @since 0.10
 */
public class ApiUtils {

	public static final String DEFAULT_BASE_URL = "https://api.minimax.chat";

	public static Consumer<HttpHeaders> getJsonContentHeaders(String apiKey) {
		return (headers) -> {
			headers.setBearerAuth(apiKey);
			headers.setContentType(MediaType.APPLICATION_JSON);
		};
	}

}
