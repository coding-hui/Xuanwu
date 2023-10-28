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
	SUCCESS(100001), FAILURE(100002), PARAM_BIND_ERROR(100003), PARAM_VALID_ERROR(100004), PARAM_ERROR(100005),
	NOT_FOUND(100006), INVALID_REQUEST(100007),

	// -------------------------------------------------------------//
	/**
	 * 通用数据层 code
	 */
	DATABASE_ERROR(100101), PRIMARY_KEI_EMPTY(100102), ENTITY_IS_NULL(100103), DATA_EXISTED(100104),
	DATA_NOT_EXIST(100105), INVALID_INDEX(100106), INVALID_ENTITY(100107), TABLE_NAME_EMPTY(100108),

	/**
	 * 通用缓存异常 code
	 */
	CACHE_KEY_CANNOT_BE_NOLL(100112), CACHE_HASH_KEY_CANNOT_BE_NOLL(100113), CACHE_HASH_FIELD_CANNOT_BE_NOLL(100114),
	CACHE_PREFIX_CANNOT_BE_NOLL(100115),;

	private final Integer code;

}
