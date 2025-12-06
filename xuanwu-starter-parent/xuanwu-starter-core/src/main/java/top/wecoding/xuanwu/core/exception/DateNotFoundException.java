package top.wecoding.xuanwu.core.exception;

/**
 * @author wecoding
 * @since 0.9
 */
public class DateNotFoundException extends BaseUncheckedException {

    public DateNotFoundException() {
        super(SystemErrorCode.DATA_NOT_EXIST);
    }

}
