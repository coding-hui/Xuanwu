package top.wecoding.xuanwu.mall.util;

import java.math.BigDecimal;
import java.util.List;
import top.wecoding.xuanwu.mall.domain.entity.Order;
import top.wecoding.xuanwu.mall.domain.entity.OrderItem;

/**
 * @author wecoding
 * @since 0.9
 */
public class PriceUtil {

  /** 计算订单应付金额 */
  public static BigDecimal calcPayAmount(Order order) {
    // 总金额+运费-促销优惠-优惠券优惠-积分抵扣
    return selfGetAmount(order.getTotalAmount())
        .add(selfGetAmount(order.getFreightAmount()))
        .subtract(selfGetAmount(order.getPromotionAmount()))
        .subtract(selfGetAmount(order.getCouponAmount()))
        .subtract(selfGetAmount(order.getIntegrationAmount()));
  }

  public static BigDecimal calcTotalAmount(Order order, List<OrderItem> orderItems) {
    BigDecimal totalAmount = new BigDecimal("0");
    for (OrderItem item : orderItems) {
      totalAmount =
          totalAmount.add(item.getFoodPrice().multiply(new BigDecimal(item.getFoodQuantity())));
    }
    return selfGetAmount(order.getTotalAmount()).add(totalAmount);
  }

  public static BigDecimal calcOrderItemTotalAmount(List<OrderItem> orderItems) {
    BigDecimal totalAmount = new BigDecimal("0");
    for (OrderItem item : orderItems) {
      totalAmount =
          totalAmount.add(item.getFoodPrice().multiply(new BigDecimal(item.getFoodQuantity())));
    }
    return totalAmount;
  }

  public static BigDecimal calcPromotionAmount(Order order, List<OrderItem> orderItems) {
    BigDecimal promotionAmount = new BigDecimal(0);
    for (OrderItem orderItem : orderItems) {
      if (orderItem.getPromotionAmount() != null) {
        promotionAmount =
            promotionAmount.add(
                orderItem
                    .getPromotionAmount()
                    .multiply(new BigDecimal(orderItem.getFoodQuantity())));
      }
    }
    return selfGetAmount(order.getPromotionAmount()).add(promotionAmount);
  }

  public static BigDecimal selfGetAmount(BigDecimal amount) {
    return amount == null ? BigDecimal.ZERO : amount;
  }
}
