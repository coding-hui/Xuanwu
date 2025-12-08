package top.wecoding.xuanwu.iam.model.response;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author wecoding
 * @since 0.10
 */
@Data
public class AuthorizationResponse {

  @JsonProperty("allowed")
  private Boolean allowed;

  @JsonProperty("denied")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private String denied;

  @JsonProperty("reason")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private String reason;

  @JsonProperty("error")
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  private String error;
}
