package top.wecoding.xuanwu.core.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author liuyuhui
 * @date 2022/9/10
 */
@Getter
@AllArgsConstructor
@Accessors(chain = true)
public enum SystemErrorCode implements ErrorCode {

	/**
	 * 通用异常 Code
	 */
	SUCCESS(0), FAILURE(999), PARAM_ERROR(100000), PARAM_MISS(100001), PARAM_TYPE_ERROR(100002),
	PARAM_BIND_ERROR(100003), PARAM_VALID_ERROR(100004), NOT_FOUND(100005), MSG_NOT_READABLE(100006),
	METHOD_NOT_SUPPORTED(100007), MEDIA_TYPE_NOT_SUPPORTED(100008), MEDIA_TYPE_NOT_ACCEPT(100009), REQ_REJECT(100010),

	// -------------------------------------------------------------//
	/**
	 * 通用网关异常
	 */
	BAD_GATEWAY(100100), SERVICE_UNAVAILABLE(100101), GATEWAY_TIMEOUT(100102), GATEWAY_FORBADE(100103),

	// -------------------------------------------------------------//
	/**
	 * 通用数据层 code
	 */
	DATA_NOT_EXIST(100200), DATA_EXISTED(100201), DATA_ADD_FAILED(100202), DATA_UPDATE_FAILED(100203),
	DATA_DELETE_FAILED(100204),

	/**
	 * 通用缓存异常 code
	 */
	CACHE_KEY_CANNOT_BE_NOLL(100205), CACHE_HASH_KEY_CANNOT_BE_NOLL(100206), CACHE_HASH_FIELD_CANNOT_BE_NOLL(100207),
	CACHE_PREFIX_CANNOT_BE_NOLL(100208),;

	private final Integer code;

}
