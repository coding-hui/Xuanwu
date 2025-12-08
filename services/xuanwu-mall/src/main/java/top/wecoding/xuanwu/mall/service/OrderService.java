package top.wecoding.xuanwu.mall.service;

import org.springframework.data.domain.Pageable;
import top.wecoding.xuanwu.core.base.PageResult;
import top.wecoding.xuanwu.mall.domain.entity.Order;
import top.wecoding.xuanwu.mall.domain.request.CreateOrderRequest;
import top.wecoding.xuanwu.mall.domain.request.OrderInfoPageRequest;
import top.wecoding.xuanwu.mall.domain.response.CreateOrderResponse;
import top.wecoding.xuanwu.mall.domain.response.OrderDetail;
import top.wecoding.xuanwu.orm.service.BaseService;

/**
 * 订单表 - Service
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-30 11:29:28
 */
public interface OrderService extends BaseService<Order, Long> {

  OrderDetail detail(Long orderId);

  PageResult<Order> listOrders(OrderInfoPageRequest queryParams, Pageable pageable);

  void cancelOrder(Long orderId);

  CreateOrderResponse createOrder(CreateOrderRequest createReq);

  void deleteOrder(Long orderId);

  void paySuccessCallback(Long orderId, Integer payType);
}
