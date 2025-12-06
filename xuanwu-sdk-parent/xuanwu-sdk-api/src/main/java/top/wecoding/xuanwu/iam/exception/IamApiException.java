package top.wecoding.xuanwu.iam.exception;

import top.wecoding.xuanwu.core.exception.BaseUncheckedException;
import top.wecoding.xuanwu.core.exception.ErrorCode;

/**
 * @author wecoding
 * @since 0.8
 */
public class IamApiException extends BaseUncheckedException {

    public IamApiException() {
        super();
    }

    public IamApiException(ErrorCode supplier, Object... args) {
        this(supplier, null, args);
    }

    public IamApiException(ErrorCode supplier, Throwable cause, Object... args) {
        this(supplier, cause, null, null, args);
    }

    public IamApiException(ErrorCode supplier, String exception, String url, Object... args) {
        this(supplier, null, exception, url, args);
    }

    public IamApiException(ErrorCode supplier, Throwable cause, String exception, String url, Object... args) {
        super(supplier, cause, exception, url, args);
    }

}
