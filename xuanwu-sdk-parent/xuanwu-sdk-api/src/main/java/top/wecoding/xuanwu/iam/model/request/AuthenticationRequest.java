package top.wecoding.xuanwu.iam.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Wecoding Liu
 * @since 0.8
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationRequest {

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

}
