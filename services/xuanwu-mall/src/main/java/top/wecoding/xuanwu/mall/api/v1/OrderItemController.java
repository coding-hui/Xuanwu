package top.wecoding.xuanwu.mall.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wecoding.xuanwu.core.annotation.Version;
import top.wecoding.xuanwu.core.base.R;
import top.wecoding.xuanwu.core.exception.DateNotFoundException;
import top.wecoding.xuanwu.mall.domain.entity.OrderItem;
import top.wecoding.xuanwu.mall.service.OrderItemService;

/**
 * 订单中所包含的商品 - API Controller
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-30 11:29:53
 */
@Version("v1")
@RequiredArgsConstructor
@RestController("orderItemController.v1")
@RequestMapping("/order/{orderId}/item")
public class OrderItemController {

  private final OrderItemService orderItemService;

  @GetMapping("/{id}")
  public R<OrderItem> getInfo(@PathVariable("id") Long id) {
    return R.ok(orderItemService.getById(id).orElseThrow(DateNotFoundException::new));
  }

  @PostMapping("")
  public R<OrderItem> create(@RequestBody @Validated OrderItem orderItem) {
    return R.ok(orderItemService.create(orderItem));
  }

  @PutMapping("/{id}")
  public R<OrderItem> update(
      @PathVariable("id") Long id, @RequestBody @Validated OrderItem orderItem) {
    return R.ok(orderItemService.updateById(id, orderItem));
  }

  @DeleteMapping("/{orderItemId}")
  public R<?> delete(
      @PathVariable("orderId") Long orderId, @PathVariable("orderItemId") Long orderItemId) {
    orderItemService.deleteOrderItem(orderId, orderItemId);
    return R.ok();
  }
}
