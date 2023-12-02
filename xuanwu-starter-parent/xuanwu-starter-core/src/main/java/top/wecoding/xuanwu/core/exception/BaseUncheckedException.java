package top.wecoding.xuanwu.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;

/**
 * base unchecked exceptions
 *
 * @author liuyuhui
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseUncheckedException extends RuntimeException {

	@Serial
	private static final long serialVersionUID = 1L;

	protected ErrorCode errorCode;

	protected Object[] args;

	public BaseUncheckedException() {
		super();
	}

	public BaseUncheckedException(String exception) {
		super(exception);
	}

	public BaseUncheckedException(Throwable cause) {
		super(cause);
	}

	public BaseUncheckedException(String exception, Throwable cause) {
		super(exception, cause);
	}

	public BaseUncheckedException(ErrorCode errorCode) {
		this(errorCode, null);
	}

	public BaseUncheckedException(ErrorCode supplier, String message) {
		this(supplier, null, message);
	}

	public BaseUncheckedException(ErrorCode errorCode, Throwable cause, String message, Object... args) {
		super(message, cause);
		this.errorCode = errorCode;
		this.args = args;
	}

}
