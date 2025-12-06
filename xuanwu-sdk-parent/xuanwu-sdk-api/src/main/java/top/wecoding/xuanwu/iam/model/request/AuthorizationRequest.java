package top.wecoding.xuanwu.iam.model.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wecoding
 * @since 0.10
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorizationRequest {

    @JsonProperty("resource")
    private String resource;

    @JsonProperty("action")
    private String action;

    @JsonProperty("subject")
    private String subject;

}
