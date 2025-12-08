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
public class RefreshTokenRequest {

  @JsonProperty("refreshToken")
  private String refreshToken;
}
