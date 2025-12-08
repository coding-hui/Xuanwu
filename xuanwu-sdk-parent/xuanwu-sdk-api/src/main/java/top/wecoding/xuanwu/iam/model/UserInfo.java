package top.wecoding.xuanwu.iam.model;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Wecoding Liu
 * @since 0.8
 */
@Data
public class UserInfo {

  @JsonProperty("metadata")
  private Metadata metadata;

  @JsonProperty("alias")
  private String alias;

  @JsonProperty("email")
  private String email;

  @JsonProperty("phone")
  private String phone;

  @JsonProperty("userType")
  private String userType;
}
