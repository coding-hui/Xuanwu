package top.wecoding.xuanwu.mall.api.v1;

import lombok.RequiredArgsConstructor;

import static org.springframework.data.domain.Sort.Direction.DESC;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.wecoding.xuanwu.core.annotation.Version;
import top.wecoding.xuanwu.core.base.R;
import top.wecoding.xuanwu.mall.domain.entity.Order;
import top.wecoding.xuanwu.mall.domain.request.CreateOrderRequest;
import top.wecoding.xuanwu.mall.domain.request.OrderInfoPageRequest;
import top.wecoding.xuanwu.mall.domain.response.CreateOrderResponse;
import top.wecoding.xuanwu.mall.domain.response.OrderDetail;
import top.wecoding.xuanwu.mall.service.OrderService;

/**
 * 订单表 - API Controller
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-30 11:29:28
 */
@Version("v1")
@RequiredArgsConstructor
@RestController("orderController.v1")
@RequestMapping("/order")
public class OrderController {

  private final OrderService orderService;

  @GetMapping("/{id}")
  public R<OrderDetail> detail(@PathVariable("id") Long id) {
    return R.ok(orderService.detail(id));
  }

  @GetMapping("")
  public R<?> paging(
      @PageableDefault(sort = "createdAt", direction = DESC) Pageable pageReq,
      OrderInfoPageRequest queryParams) {
    return R.ok(orderService.listOrders(queryParams, pageReq));
  }

  @PostMapping("")
  public R<CreateOrderResponse> create(@RequestBody @Validated CreateOrderRequest createReq) {
    return R.ok(orderService.createOrder(createReq));
  }

  @PutMapping("/{id}")
  public R<Order> update(@PathVariable("id") Long id, @RequestBody @Validated Order order) {
    return R.ok(orderService.updateById(id, order));
  }

  @DeleteMapping("/{id}")
  public R<?> delete(@PathVariable("id") Long id) {
    orderService.deleteOrder(id);
    return R.ok();
  }

  @GetMapping("/cancel_order/{id}")
  public R<?> cancelOrder(@PathVariable("id") Long id) {
    orderService.cancelOrder(id);
    return R.ok();
  }

  @GetMapping("/pay_success")
  public R<?> paySuccessCallback(
      @RequestParam("orderId") Long orderId, @RequestParam("payType") Integer payType) {
    orderService.paySuccessCallback(orderId, payType);
    return R.ok();
  }
}
