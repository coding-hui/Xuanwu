package top.wecoding.xuanwu.mall.api.v1;

import static org.springframework.data.domain.Sort.Direction.DESC;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.bind.annotation.RestController;
import top.wecoding.xuanwu.core.annotation.Version;
import top.wecoding.xuanwu.core.base.R;
import top.wecoding.xuanwu.core.exception.DateNotFoundException;
import top.wecoding.xuanwu.mall.constant.OrderTableStatus;
import top.wecoding.xuanwu.mall.domain.entity.OrderTable;
import top.wecoding.xuanwu.mall.repository.OrderTableRepository;
import top.wecoding.xuanwu.mall.service.OrderTableService;

/**
 * 订单桌号 - API Controller
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-29 20:43:29
 */
@Version("v1")
@RequiredArgsConstructor
@RestController("orderTableController.v1")
@RequestMapping("/order_tables")
public class OrderTableController {

  private final OrderTableService orderTableService;

  private final OrderTableRepository orderTableRepository;

  @GetMapping("/{id}")
  public R<OrderTable> getInfo(@PathVariable("id") Long id) {
    return R.ok(orderTableService.getById(id).orElseThrow(DateNotFoundException::new));
  }

  @GetMapping("")
  public R<?> paging(
      @PageableDefault(sort = "status", direction = DESC) Pageable pageReq, OrderTable params) {
    return R.ok(orderTableService.listOrderTables(params, pageReq));
  }

  @PostMapping("")
  public R<OrderTable> create(@RequestBody @Validated OrderTable orderTable) {
    if (orderTable.getStatus() == null) {
      orderTable.setStatus(OrderTableStatus.AVAILABLE.getStatus());
    }
    if (orderTableRepository.existsByCode(orderTable.getCode())) {
      return R.error("订单桌号【" + orderTable.getCode() + "】已存在");
    }
    return R.ok(orderTableService.create(orderTable));
  }

  @PutMapping("/{id}")
  public R<OrderTable> update(
      @PathVariable("id") Long id, @RequestBody @Validated OrderTable orderTable) {
    Optional<OrderTable> old = orderTableService.getById(id);
    if (old.isEmpty()) {
      return R.error("订单桌号【" + orderTable.getCode() + "】不存在");
    }
    String oldCode = old.get().getCode();
    if (!oldCode.equals(orderTable.getCode())
        && orderTableRepository.existsByCode(orderTable.getCode())) {
      return R.error("订单桌号【" + orderTable.getCode() + "】已存在");
    }
    return R.ok(orderTableService.updateById(id, orderTable));
  }

  @DeleteMapping("/{id}")
  public R<?> delete(@PathVariable("id") Long id) {
    orderTableService.deleteById(id);
    return R.ok();
  }

  @DeleteMapping("/completed/{id}")
  public R<?> completed(@PathVariable("id") Long id) {
    orderTableService.completedOrderTable(id);
    return R.ok();
  }
}
