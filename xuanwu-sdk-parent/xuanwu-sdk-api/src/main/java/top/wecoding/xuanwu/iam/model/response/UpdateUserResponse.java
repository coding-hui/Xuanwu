package top.wecoding.xuanwu.iam.model.response;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;
import top.wecoding.xuanwu.iam.model.UserInfo;

/**
 * @author Wecoding Liu
 * @since 0.8
 */
@Data
public class UpdateUserResponse {

  @JsonProperty("code")
  private int code;

  @JsonProperty("msg")
  private String msg;

  @JsonProperty("data")
  private UserInfo userInfo;
}
