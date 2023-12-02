package top.wecoding.xuanwu.iam.api;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import top.wecoding.xuanwu.core.base.R;
import top.wecoding.xuanwu.iam.model.TokenInfo;
import top.wecoding.xuanwu.iam.model.UserInfo;
import top.wecoding.xuanwu.iam.model.request.AuthenticationRequest;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author wecoding
 * @since 0.8
 */
public interface AuthenticationApi {

	@PostExchange("/api/v1/login")
	R<TokenInfo> authenticate(@RequestBody AuthenticationRequest request);

	@GetExchange("/api/v1/auth/user-info")
	R<UserInfo> currentUserInfo();

	@GetExchange("/api/v1/auth/user-info")
	R<UserInfo> currentUserInfo(@RequestHeader(value = AUTHORIZATION, required = false) String accessToken);

}
