package top.wecoding.xuanwu.core.exception;

import top.wecoding.xuanwu.core.message.I18n;

/** base error code supplier */
public interface ErrorCode {

  Integer getCode();

  default String getDesc(String defaultMessage) {
    return I18n.getMessage(getCode().toString(), defaultMessage);
  }

  default String getDesc(String defaultMessage, Object... args) {
    return I18n.getMessage(getCode().toString(), args, defaultMessage);
  }

  default String getDesc(Object... args) {
    return I18n.getMessage(getCode().toString(), args);
  }
}
