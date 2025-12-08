package top.wecoding.xuanwu.sdk.examples;

import java.util.UUID;
import top.wecoding.xuanwu.core.base.R;
import top.wecoding.xuanwu.iam.api.AuthenticationApi;
import top.wecoding.xuanwu.iam.api.UserApi;
import top.wecoding.xuanwu.iam.client.ApiClient;
import top.wecoding.xuanwu.iam.client.Clients;
import top.wecoding.xuanwu.iam.model.TokenInfo;
import top.wecoding.xuanwu.iam.model.UserInfo;
import top.wecoding.xuanwu.iam.model.request.AuthenticationRequest;
import top.wecoding.xuanwu.iam.model.request.CreateUserRequest;
import top.wecoding.xuanwu.iam.model.request.UpdateUserRequest;

/**
 * @author wecoding
 * @since 0.8
 */
public class Quickstart {

  public static void main(String[] args) {
    final String name =
        "quickstart-" + UUID.randomUUID().toString().replaceAll("-", "").substring(0, 20);
    final String email = "quickstart@example.com";
    final String password = "WeCoding@.123";

    String basePath = "http://iam.wecoding.top";
    if (args.length > 0 && !args[0].isEmpty()) {
      basePath = args[0];
    }

    ApiClient apiClient = Clients.builder().setApiBase(basePath).build();

    AuthenticationApi authenticationApi = apiClient.authenticationApi();

    AuthenticationRequest authenticationRequest = new AuthenticationRequest("ADMIN", "WECODING");
    R<TokenInfo> authenticateResponse = authenticationApi.authenticate(authenticationRequest);
    if (R.checkSuccess(authenticateResponse)) {
      println("authenticate success.");
    }

    String accessToken = authenticateResponse.getData().getAccessToken();
    apiClient.setApiToken(accessToken);

    R<UserInfo> apiResult = authenticationApi.currentUserInfo();
    if (R.checkSuccess(apiResult)) {
      UserInfo userInfo = apiResult.getData();
      println("get current user info success: " + userInfo);
    }

    UserApi userApi = apiClient.userApi();

    CreateUserRequest createUserReq =
        CreateUserRequest.builder()
            .name(name)
            .email(email)
            .alias("quickstart")
            .password(password)
            .build();
    R<UserInfo> createUserResult = userApi.createUser(createUserReq);
    UserInfo createdUser = createUserResult.getData();
    println("create user success: " + createdUser);

    UpdateUserRequest updateReq =
        UpdateUserRequest.builder()
            .phone("19088889999")
            .email(email)
            .alias("update-quickstart")
            .password(password)
            .build();
    R<UserInfo> updateUserResult =
        userApi.updateUser(createdUser.getMetadata().getInstanceId(), updateReq);
    println("update user success: " + updateUserResult);

    R<UserInfo> userInfoResponse = userApi.getUserInfo(createdUser.getMetadata().getInstanceId());
    println("get user info success. resp = " + userInfoResponse.getData());

    userApi.disableUser(createdUser.getMetadata().getInstanceId());

    userApi.enableUser(createdUser.getMetadata().getInstanceId());

    userApi.deleteUserById(createdUser.getMetadata().getInstanceId());
    println("delete user success.");
  }

  private static void println(String message) {
    System.out.println(message);
    System.out.flush();
  }
}
