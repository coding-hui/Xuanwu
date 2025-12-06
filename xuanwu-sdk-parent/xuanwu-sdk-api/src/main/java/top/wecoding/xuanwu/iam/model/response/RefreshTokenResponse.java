package top.wecoding.xuanwu.iam.model.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import top.wecoding.xuanwu.iam.model.TokenInfo;

/**
 * @author wecoding
 * @since 0.8
 */
@Data
public class RefreshTokenResponse {

    @JsonProperty("code")
    private int code;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("data")
    private TokenInfo tokenInfo;

}
