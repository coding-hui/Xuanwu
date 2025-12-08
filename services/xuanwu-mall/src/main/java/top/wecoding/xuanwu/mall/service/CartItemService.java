package top.wecoding.xuanwu.mall.service;

import java.util.List;

import top.wecoding.xuanwu.mall.domain.entity.CartItem;
import top.wecoding.xuanwu.mall.domain.request.CartItemListRequest;
import top.wecoding.xuanwu.mall.domain.request.UpdateFoodQuantityRequest;
import top.wecoding.xuanwu.orm.service.BaseService;

/**
 * 购物车表 - Service
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-30 12:20:12
 */
public interface CartItemService extends BaseService<CartItem, Long> {

  List<CartItem> listCartItems(CartItemListRequest listRequest);

  void updateFoodQuantity(UpdateFoodQuantityRequest updateReq);

  void addCartItem(CartItem cartItem);

  void batchAddCartItem(List<CartItem> cartItems);

  boolean deleteCartItem(String tableCode, Long foodId);
}
