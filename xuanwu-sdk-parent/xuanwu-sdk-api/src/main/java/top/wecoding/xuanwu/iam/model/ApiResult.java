package top.wecoding.xuanwu.iam.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import lombok.Data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Wecoding Liu
 * @since 0.8
 */
@Data
public class ApiResult<T> implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  private static final String ERROR = "error";

  private static final String SUCCESS = "success";

  private static final int SUCCESS_CODE = 100001;

  @JsonProperty("code")
  private int code;

  @JsonProperty("msg")
  private String msg = SUCCESS;

  @JsonProperty("data")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private T data;

  @JsonProperty("request_id")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String requestId;

  @JsonProperty("headers")
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Map<String, String> headers;

  public static <T> boolean isSuccessCode(ApiResult<T> apiResult) {
    return apiResult != null && SUCCESS_CODE == apiResult.getCode();
  }

  public static <T> boolean isSuccess(ApiResult<T> apiResult) {
    if (!isSuccessCode(apiResult)) {
      return false;
    }
    T data = apiResult.getData();
    if (data == null) {
      return false;
    }
    if (data instanceof Collection<?>) {
      return ((Collection<?>) data).isEmpty();
    }
    return true;
  }
}
