package top.wecoding.xuanwu.iam.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Wecoding Liu
 * @since 0.8
 */
@Data
public class RefreshTokenRequest {

	@JsonProperty("refreshToken")
	private String refreshToken;

	public RefreshTokenRequest() {
	}

	public RefreshTokenRequest(String refreshToken) {
		this.refreshToken = refreshToken;
	}

}
