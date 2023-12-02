package top.wecoding.xuanwu.iam.client;

import lombok.Setter;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.HttpMessageConverterExtractor;
import top.wecoding.xuanwu.core.base.R;
import top.wecoding.xuanwu.core.exception.UnauthorizedException;
import top.wecoding.xuanwu.core.message.I18n;
import top.wecoding.xuanwu.iam.exception.IamApiException;

import java.io.IOException;
import java.util.List;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * @author wecoding
 * @since 0.8
 */
public class ApiResponseErrorHandler extends DefaultResponseErrorHandler {

	@Setter
	private List<HttpMessageConverter<?>> messageConverters;

	public ApiResponseErrorHandler() {
		this(List.of(new MappingJackson2HttpMessageConverter(), new FormHttpMessageConverter(),
				new StringHttpMessageConverter()));
	}

	public ApiResponseErrorHandler(List<HttpMessageConverter<?>> messageConverters) {
		this.messageConverters = messageConverters;
	}

	@Override
	public void handleError(ClientHttpResponse response, HttpStatusCode statusCode) throws IOException {
		R<?> apiResult = extract(response);
		if (apiResult == null) {
			super.handleError(response, statusCode);
			return;
		}
		if (UNAUTHORIZED.isSameCodeAs(statusCode)) {
			throw new UnauthorizedException(apiResult::getCode, apiResult.getMsg());
		}
		String message = I18n.getMessage(apiResult.getCode().toString(), apiResult.getMsg());
		throw new IamApiException(apiResult::getCode, message);
	}

	private R<?> extract(ClientHttpResponse response) throws IOException {
		var extractor = new HttpMessageConverterExtractor<>(R.class, this.messageConverters);
		return extractor.extractData(response);
	}

}
