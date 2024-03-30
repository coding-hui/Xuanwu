package top.wecoding.xuanwu.iam.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import top.wecoding.xuanwu.iam.model.TokenInfo;

/**
 * @author Wecoding Liu
 * @since 0.8
 */
@Data
public class AuthenticationResponse {

	@JsonProperty("code")
	private int code;

	@JsonProperty("msg")
	private String msg;

	@JsonProperty("data")
	private TokenInfo tokenInfo;

}