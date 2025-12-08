package top.wecoding.xuanwu.codegen.api.controller.v1;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wecoding.xuanwu.core.annotation.Version;
import top.wecoding.xuanwu.core.base.R;
import top.wecoding.xuanwu.iam.api.AuthenticationApi;

/**
 * @author wecoding
 * @since 0.8
 */
@Version("v1")
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserInfoController {

  private final AuthenticationApi authenticationApi;

  @GetMapping("/user-info")
  public R<?> currentUserInfo(
      @CookieValue(value = "IAM_TOKEN", required = false) String accessToken) {
    return authenticationApi.currentUserInfo(String.format("Bearer %s", accessToken));
  }
}
