package top.wecoding.xuanwu.iam.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Wecoding Liu
 * @since 0.8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {

  @JsonProperty("name")
  private String name;

  @JsonProperty("password")
  private String password;

  @JsonProperty("alias")
  private String alias;

  @JsonProperty("email")
  private String email;

  @JsonProperty("phone")
  private String phone;

  @JsonProperty("userType")
  private String userType;
}
