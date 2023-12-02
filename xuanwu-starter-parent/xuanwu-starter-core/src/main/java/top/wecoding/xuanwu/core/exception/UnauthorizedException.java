package top.wecoding.xuanwu.core.exception;

public class UnauthorizedException extends BaseUncheckedException {

	public UnauthorizedException() {
	}

	public UnauthorizedException(ErrorCode supplier) {
		this(supplier, null);
	}

	public UnauthorizedException(ErrorCode supplier, String message) {
		super(supplier, message);
	}

}
