package top.wecoding.xuanwu.codegen.api.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wecoding.xuanwu.core.base.R;
import top.wecoding.xuanwu.iam.api.AuthenticationApi;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

/**
 * @author wecoding
 * @since 0.8
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/user-info")
public class UserInfoController {

	private final AuthenticationApi authenticationApi;

	@GetMapping
	public R<?> currentUserInfo(@RequestHeader(value = AUTHORIZATION, required = false) String accessToken) {
		return authenticationApi.currentUserInfo(accessToken);
	}

}
