package top.wecoding.xuanwu.mall.api.v1;

import java.util.List;

import lombok.RequiredArgsConstructor;
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
import top.wecoding.xuanwu.core.exception.DateNotFoundException;
import top.wecoding.xuanwu.mall.domain.entity.CartItem;
import top.wecoding.xuanwu.mall.domain.request.CartItemListRequest;
import top.wecoding.xuanwu.mall.domain.request.UpdateFoodQuantityRequest;
import top.wecoding.xuanwu.mall.service.CartItemService;

/**
 * 购物车表 - API Controller
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-30 12:20:12
 */
@Version("v1")
@RequiredArgsConstructor
@RestController("cartItemController.v1")
@RequestMapping("/cart")
public class CartItemController {

  private final CartItemService cartItemService;

  @GetMapping("/{id}")
  public R<CartItem> getInfo(@PathVariable("id") Long id) {
    return R.ok(cartItemService.getById(id).orElseThrow(DateNotFoundException::new));
  }

  @GetMapping("")
  public R<?> listing(CartItemListRequest listRequest) {
    return R.ok(cartItemService.listCartItems(listRequest));
  }

  @PostMapping("")
  public R<CartItem> create(@RequestBody @Validated CartItem cartItem) {
    cartItemService.addCartItem(cartItem);
    return R.ok();
  }

  @PostMapping("/batch_add")
  public R<CartItem> batchCreate(@RequestBody @Validated List<CartItem> cartItems) {
    cartItemService.batchAddCartItem(cartItems);
    return R.ok();
  }

  @PutMapping("/{id}")
  public R<CartItem> update(
      @PathVariable("id") Long id, @RequestBody @Validated CartItem cartItem) {
    return R.ok(cartItemService.updateById(id, cartItem));
  }

  @PutMapping("/update_quantity")
  public R<CartItem> updateQuantity(@RequestBody @Validated UpdateFoodQuantityRequest updateReq) {
    cartItemService.updateFoodQuantity(updateReq);
    return R.ok();
  }

  @DeleteMapping("/{id}")
  public R<?> delete(@PathVariable("id") Long id) {
    cartItemService.deleteById(id);
    return R.ok();
  }

  @DeleteMapping("/delete_item")
  public R<?> deleteCartItem(
      @RequestParam("foodId") Long foodId, @RequestParam("tableCode") String tableCode) {
    return R.ok(cartItemService.deleteCartItem(tableCode, foodId));
  }
}
