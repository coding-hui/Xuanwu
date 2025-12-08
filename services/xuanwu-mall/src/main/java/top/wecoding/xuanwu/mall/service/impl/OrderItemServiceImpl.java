package top.wecoding.xuanwu.mall.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import top.wecoding.xuanwu.core.exception.SystemErrorCode;
import top.wecoding.xuanwu.core.util.ArgumentAssert;
import top.wecoding.xuanwu.mall.domain.entity.Order;
import top.wecoding.xuanwu.mall.domain.entity.OrderItem;
import top.wecoding.xuanwu.mall.repository.OrderItemRepository;
import top.wecoding.xuanwu.mall.repository.OrderRepository;
import top.wecoding.xuanwu.mall.service.OrderItemService;
import top.wecoding.xuanwu.orm.service.BaseServiceImpl;

/**
 * 订单中所包含的商品 - ServiceImpl
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-30 11:29:53
 */
@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl extends BaseServiceImpl<OrderItem, Long>
    implements OrderItemService {

  private final OrderItemRepository orderItemRepository;

  private final OrderRepository orderRepository;

  @Override
  public void deleteOrderItem(Long orderId, Long orderItemId) {
    Order order = orderRepository.getReferenceById(orderId);

    ArgumentAssert.notNull(order, SystemErrorCode.DATA_NOT_EXIST);

    OrderItem orderItem = orderItemRepository.getReferenceById(orderItemId);

    ArgumentAssert.notNull(orderItem, SystemErrorCode.DATA_NOT_EXIST);

    int quantity = orderItem.getFoodQuantity();
    quantity = quantity - 1;

    order.setTotalAmount(order.getTotalAmount().subtract(orderItem.getFoodPrice()));

    if (quantity == 0) {
      orderItemRepository.deleteById(orderItemId);
    } else {
      orderItem.setFoodQuantity(quantity);
      orderItemRepository.save(orderItem);
    }
  }

  @Override
  protected JpaRepository<OrderItem, Long> getBaseRepository() {
    return this.orderItemRepository;
  }
}
