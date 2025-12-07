package org.springframework.web.accept;

import top.wecoding.xuanwu.core.exception.BaseUncheckedException;
import top.wecoding.xuanwu.core.exception.ErrorCode;
import top.wecoding.xuanwu.core.exception.SystemErrorCode;

/**
 * 无效的 API 版本异常
 *
 * @author liuyuhui
 */
public class InvalidApiVersionException extends BaseUncheckedException {

    public InvalidApiVersionException() {
        super(SystemErrorCode.INVALID_API_VERSION);
    }

    public InvalidApiVersionException(ErrorCode errorCode) {
        super(errorCode);
    }

    public InvalidApiVersionException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public InvalidApiVersionException(String message) {
        super(SystemErrorCode.INVALID_API_VERSION, message);
    }

    public InvalidApiVersionException(ErrorCode errorCode, Throwable cause, String message, String url,
            Object... args) {
        super(errorCode, cause, message, url, args);
    }
}
