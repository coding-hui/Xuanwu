package top.wecoding.xuanwu.sdk.examples;

import top.wecoding.xuanwu.core.base.R;
import top.wecoding.xuanwu.iam.api.AuthenticationApi;
import top.wecoding.xuanwu.iam.client.ApiClient;
import top.wecoding.xuanwu.iam.client.Clients;
import top.wecoding.xuanwu.iam.model.TokenInfo;
import top.wecoding.xuanwu.iam.model.UserInfo;
import top.wecoding.xuanwu.iam.model.request.AuthenticationRequest;

/**
 * @author wecoding
 * @since 0.8
 */
public class Quickstart {

	public static void main(String[] args) {
		String basePath = "http://iam.wecoding.local";
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
	}

	private static void println(String message) {
		System.out.println(message);
		System.out.flush();
	}

}
