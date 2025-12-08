package top.wecoding.xuanwu.core.exception;

/**
 * 业务异常
 *
 * @author liuyuhui
 * @date 2022/04/04
 */
public class ServerException extends BaseUncheckedException {

  public ServerException() {
    super();
  }

  public ServerException(ErrorCode supplier, Object... args) {
    this(supplier, null, args);
  }

  public ServerException(ErrorCode supplier, Throwable cause, Object... args) {
    this(supplier, cause, null, null, args);
  }

  public ServerException(ErrorCode supplier, String exception, String url, Object... args) {
    this(supplier, null, exception, url, args);
  }

  public ServerException(
      ErrorCode supplier, Throwable cause, String exception, String url, Object... args) {
    super(supplier, cause, exception, url, args);
  }
}
