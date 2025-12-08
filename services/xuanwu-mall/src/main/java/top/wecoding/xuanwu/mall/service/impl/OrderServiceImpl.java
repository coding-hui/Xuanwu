package top.wecoding.xuanwu.mall.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static top.wecoding.xuanwu.core.exception.SystemErrorCode.DATA_NOT_EXIST;
import static top.wecoding.xuanwu.core.exception.SystemErrorCode.PARAM_ERROR;
import static top.wecoding.xuanwu.mall.util.PriceUtil.calcPayAmount;
import static top.wecoding.xuanwu.mall.util.PriceUtil.calcPromotionAmount;
import static top.wecoding.xuanwu.mall.util.PriceUtil.calcTotalAmount;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wecoding.xuanwu.core.base.PageResult;
import top.wecoding.xuanwu.core.exception.IllegalParameterException;
import top.wecoding.xuanwu.core.exception.SystemErrorCode;
import top.wecoding.xuanwu.core.util.ArgumentAssert;
import top.wecoding.xuanwu.mall.constant.OrderStatus;
import top.wecoding.xuanwu.mall.constant.OrderTableStatus;
import top.wecoding.xuanwu.mall.domain.converter.OrderItemConverter;
import top.wecoding.xuanwu.mall.domain.entity.CartItem;
import top.wecoding.xuanwu.mall.domain.entity.Order;
import top.wecoding.xuanwu.mall.domain.entity.OrderItem;
import top.wecoding.xuanwu.mall.domain.entity.OrderTable;
import top.wecoding.xuanwu.mall.domain.request.CreateOrderRequest;
import top.wecoding.xuanwu.mall.domain.request.OrderInfoPageRequest;
import top.wecoding.xuanwu.mall.domain.response.CreateOrderResponse;
import top.wecoding.xuanwu.mall.domain.response.OrderDetail;
import top.wecoding.xuanwu.mall.repository.CartItemRepository;
import top.wecoding.xuanwu.mall.repository.OrderItemRepository;
import top.wecoding.xuanwu.mall.repository.OrderRepository;
import top.wecoding.xuanwu.mall.repository.OrderTableRepository;
import top.wecoding.xuanwu.mall.service.OrderService;
import top.wecoding.xuanwu.mall.service.OrderTableService;
import top.wecoding.xuanwu.mall.service.PrinterService;
import top.wecoding.xuanwu.orm.helper.QueryHelp;
import top.wecoding.xuanwu.orm.service.BaseServiceImpl;

/**
 * 订单表 - ServiceImpl
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-30 11:29:28
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl extends BaseServiceImpl<Order, Long> implements OrderService {

  private final OrderRepository orderRepository;

  private final OrderItemRepository orderItemRepository;

  private final CartItemRepository cartItemRepository;

  private final OrderItemConverter orderItemConverter;

  private final OrderTableService orderTableService;

  private final PrinterService printerService;

  private final OrderTableRepository orderTableRepository;

  @Override
  public OrderDetail detail(Long orderId) {
    Optional<Order> order = orderRepository.findById(orderId);
    if (order.isEmpty()) {
      ArgumentAssert.error(SystemErrorCode.DATA_NOT_EXIST);
    }
    List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
    return OrderDetail.builder().order(order.get()).orderItems(orderItems).build();
  }

  @Override
  public PageResult<Order> listOrders(OrderInfoPageRequest queryParams, Pageable pageable) {
    Page<Order> pageResult =
        this.orderRepository.findAll(
            (root, criteriaQuery, criteriaBuilder) ->
                QueryHelp.getPredicate(root, queryParams, criteriaBuilder),
            pageable);
    return PageResult.of(pageResult.getContent(), pageResult.getTotalElements());
  }

  @Override
  public void cancelOrder(Long orderId) {
    Optional<Order> order = orderRepository.findById(orderId);
    if (order.isEmpty()) {
      return;
    }
    order.get().setStatus(OrderStatus.CANCEL.getCode());
    orderRepository.save(order.get());
    orderTableService.updateStatusByCode(
        order.get().getTableCode(), OrderTableStatus.AVAILABLE.getStatus());
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public CreateOrderResponse createOrder(CreateOrderRequest createReq) {
    // 获取订单桌号
    OrderTable orderTable = orderTableRepository.getByCode(createReq.getTableCode());

    // 获取订单桌号的菜品
    List<CartItem> cartItems = cartItemRepository.findByTableCode(createReq.getTableCode());

    ArgumentAssert.notEmpty(cartItems, "菜品不能空为");

    List<OrderItem> orderItems =
        cartItems.stream().map(orderItemConverter::cartItemToEntity).toList();

    // 创建订单
    Order order = new Order();
    if (createReq.getOrderId() != null) {
      order = orderRepository.findById(createReq.getOrderId()).orElseGet(Order::new);
    }
    // 初始化一些信息
    if (order.getId() == null) {
      order = new Order();
      order.setDiscountAmount(new BigDecimal(0));
      order.setFreightAmount(new BigDecimal(0));
      // 订单来源：0->PC订单；1->app订单
      order.setSourceType(0);
      // 订单状态：0->待付款；1->已完成；2->已关闭
      order.setStatus(0);
      // 订单类型：0->正常订单；1->秒杀订单
      order.setOrderType(0);
      // 0->未支付；1->支付宝；2->微信
      order.setPayType(createReq.getPayType() == null ? 0 : createReq.getPayType());
      // 生成订单号
      order.setOrderSn(generateOrderSn(order));
    }
    order.setPeopleCount(orderTable.getNumberOfDiners());
    order.setTableCode(createReq.getTableCode());
    order.setPayAmount(calcPayAmount(order));
    order.setPromotionAmount(calcPromotionAmount(order, orderItems));
    order.setTotalAmount(calcTotalAmount(order, orderItems));

    // 插入order表和order_item表
    orderRepository.save(order);
    for (OrderItem orderItem : orderItems) {
      OrderItem oldItem =
          orderItemRepository.findByOrderIdAndFoodId(order.getId(), orderItem.getFoodId());
      if (oldItem != null) {
        orderItem.setId(oldItem.getId());
        orderItem.setFoodQuantity(orderItem.getFoodQuantity() + oldItem.getFoodQuantity());
      }
      orderItem.setOrderId(order.getId());
      orderItem.setOrderSn(order.getOrderSn());
    }
    orderItemRepository.saveAll(orderItems);

    // 清空购物车
    cartItemRepository.deleteAll(cartItems);

    // 更新订单桌号状态
    orderTableService.updateStatusByCode(
        createReq.getTableCode(), OrderTableStatus.IN_USE.getStatus());

    // 打印小票
    // if (createReq.getPrintSalesTicket() != null && createReq.getPrintSalesTicket())
    // {
    // printerService.printSalesTicket(OrderDetail.builder().order(order).orderItems(orderItems).build());
    // }

    return CreateOrderResponse.builder().order(order).orderItems(orderItems).build();
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteOrder(Long orderId) {
    var order =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new IllegalParameterException(DATA_NOT_EXIST));
    Integer status = order.getStatus();
    if (OrderStatus.PENDING_PAYMENT.getCode() == status) {
      ArgumentAssert.error(PARAM_ERROR, "只能删除已完成或已关闭的订单！");
    }
    orderRepository.deleteById(orderId);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void paySuccessCallback(Long orderId, Integer payType) {
    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new IllegalParameterException(PARAM_ERROR));
    order.setStatus(OrderStatus.COMPLETED.getCode());
    order.setPayType(payType);
    order.setPaymentTime(new Date());
    orderRepository.save(order);
    orderTableService.updateStatusByCode(
        order.getTableCode(), OrderTableStatus.AVAILABLE.getStatus());
  }

  @Override
  protected JpaRepository<Order, Long> getBaseRepository() {
    return this.orderRepository;
  }

  /** 生成18位订单编号:8位日期+2位平台号码+2位支付方式+6位以上自增id */
  private String generateOrderSn(Order order) {
    StringBuilder sb = new StringBuilder();
    String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
    String uuid = UUID.randomUUID().toString().replace("-", "").substring(0, 6);
    sb.append(date);
    sb.append(String.format("%02d", order.getSourceType()));
    sb.append(String.format("%02d", order.getPayType()));
    sb.append(uuid);
    return sb.toString();
  }
}
