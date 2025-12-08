package top.wecoding.xuanwu.iam.api;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;
import org.springframework.web.service.annotation.PutExchange;
import top.wecoding.xuanwu.core.base.R;
import top.wecoding.xuanwu.iam.model.UserInfo;
import top.wecoding.xuanwu.iam.model.request.CreateUserRequest;
import top.wecoding.xuanwu.iam.model.request.UpdateUserRequest;

/**
 * @author wecoding
 * @since 0.8
 */
public interface UserApi {

  @GetExchange("/api/v1/users/{userId}")
  R<UserInfo> getUserInfo(@PathVariable("userId") String userId);

  @PostExchange("/api/v1/users")
  R<UserInfo> createUser(@RequestBody CreateUserRequest createUserRequest);

  @PutExchange("/api/v1/users/{userId}")
  R<UserInfo> updateUser(
      @PathVariable("userId") String userId, @RequestBody UpdateUserRequest updateUserRequest);

  @DeleteExchange("/api/v1/users/{userId}")
  void deleteUserById(@PathVariable("userId") String userId);

  @GetExchange("/api/v1/users/{userId}")
  void enableUser(@PathVariable("userId") String userId);

  @GetExchange("/api/v1/users/{userId}")
  void disableUser(@PathVariable("userId") String userId);
}
