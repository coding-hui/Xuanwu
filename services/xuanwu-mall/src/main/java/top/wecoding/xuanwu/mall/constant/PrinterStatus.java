package top.wecoding.xuanwu.mall.constant;

import lombok.Getter;

/**
 * @author wecoding
 * @since 0.9
 */
@Getter
public enum PrinterStatus {
  DISABLED(0),
  AVAILABLE(1);

  private final Integer status;

  PrinterStatus(Integer status) {
    this.status = status;
  }

  public boolean is(Integer status) {
    if (status == null) {
      return false;
    }
    return this.status.equals(status);
  }
}
