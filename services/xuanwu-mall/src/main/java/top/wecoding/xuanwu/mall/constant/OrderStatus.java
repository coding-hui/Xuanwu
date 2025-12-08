package top.wecoding.xuanwu.mall.constant;

import lombok.Getter;

/**
 * @author wecoding
 * @since 0.9
 */
@Getter
public enum OrderStatus {
  PENDING_PAYMENT(0, "待付款"),
  COMPLETED(1, "已完成"),
  CANCEL(2, "已关闭");

  private final int code;

  private final String text;

  OrderStatus(int code, String text) {
    this.code = code;
    this.text = text;
  }
}
