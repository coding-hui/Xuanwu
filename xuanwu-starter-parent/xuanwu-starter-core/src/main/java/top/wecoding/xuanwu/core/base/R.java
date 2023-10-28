package top.wecoding.xuanwu.core.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import top.wecoding.xuanwu.core.exception.SystemErrorCode;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wecoding
 * @since 0.9
 */
@Getter
@Setter
public class R<T> implements Serializable {

	@Serial
	private static final long serialVersionUID = -5623650085162033777L;

	private static final String ERROR = "error";

	private static final String SUCCESS = "success";

	@JsonProperty("code")
	private Integer code;

	@JsonProperty("msg")
	private String msg = SUCCESS;

	@JsonProperty("success")
	private boolean success;

	@JsonProperty("data")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T data;

	@JsonProperty("request_id")
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String requestId;

	public R() {
	}

	public R(boolean success, Integer code) {
		this(success, code, null, SUCCESS);
	}

	public R(boolean success, T data) {
		this(success, SystemErrorCode.SUCCESS.getCode(), data, SUCCESS);
	}

	public R(boolean success, Integer code, T data) {
		this(success, code, data, SUCCESS);
	}

	public R(boolean success, Integer code, T data, String msg) {
		this.success = success;
		this.code = code;
		this.data = data;
		this.msg = msg;
	}

	public static <T> R<T> ok() {
		return new R<>(true, SystemErrorCode.SUCCESS.getCode(), null);
	}

	public static <T> R<T> ok(T data) {
		return new R<>(true, SystemErrorCode.SUCCESS.getCode(), data);
	}

	public static <T> R<T> ok(T data, String msg) {
		return new R<>(true, SystemErrorCode.SUCCESS.getCode(), data, msg);
	}

}
