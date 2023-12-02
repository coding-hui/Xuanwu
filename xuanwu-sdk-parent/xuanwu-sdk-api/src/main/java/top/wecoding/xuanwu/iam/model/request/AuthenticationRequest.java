package top.wecoding.xuanwu.iam.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author Wecoding Liu
 * @since 0.8
 */
@Data
public class AuthenticationRequest {

	@JsonProperty("username")
	private String username;

	@JsonProperty("password")
	private String password;

	public AuthenticationRequest() {
	}

	public AuthenticationRequest(String username, String password) {
		this.username = username;
		this.password = password;
	}

}
