package top.wecoding.xuanwu.iam.api;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import top.wecoding.xuanwu.core.base.R;
import top.wecoding.xuanwu.iam.model.TokenInfo;
import top.wecoding.xuanwu.iam.model.UserInfo;
import top.wecoding.xuanwu.iam.model.request.AuthenticationRequest;
import top.wecoding.xuanwu.iam.model.request.AuthorizationRequest;
import top.wecoding.xuanwu.iam.model.response.AuthorizationResponse;

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
  R<UserInfo> currentUserInfo(
      @RequestHeader(value = AUTHORIZATION, required = false) String accessToken);

  @PostExchange("/api/v1/authz")
  R<AuthorizationResponse> authorize(@RequestBody AuthorizationRequest request);

  default R<AuthorizationResponse> authorize(String subject, String resource, String action) {
    return authorize(
        AuthorizationRequest.builder().subject(subject).resource(resource).action(action).build());
  }
}
