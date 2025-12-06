package top.wecoding.xuanwu.mall.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wecoding.xuanwu.core.exception.SystemErrorCode;
import top.wecoding.xuanwu.core.util.ArgumentAssert;
import top.wecoding.xuanwu.mall.domain.entity.CartItem;
import top.wecoding.xuanwu.mall.domain.request.CartItemListRequest;
import top.wecoding.xuanwu.mall.domain.request.UpdateFoodQuantityRequest;
import top.wecoding.xuanwu.mall.repository.CartItemRepository;
import top.wecoding.xuanwu.mall.service.CartItemService;
import top.wecoding.xuanwu.orm.helper.QueryHelp;
import top.wecoding.xuanwu.orm.service.BaseServiceImpl;

import java.util.List;

/**
 * 购物车表 - ServiceImpl
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-30 12:20:12
 */
@Service
@RequiredArgsConstructor
public class CartItemServiceImpl extends BaseServiceImpl<CartItem, Long> implements CartItemService {

    private final CartItemRepository cartItemRepository;

    @Override
    public List<CartItem> listCartItems(CartItemListRequest listRequest) {
        return this.cartItemRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, listRequest, criteriaBuilder));
    }

    @Override
    public void updateFoodQuantity(UpdateFoodQuantityRequest updateReq) {
        CartItem cartItem = cartItemRepository.getByTableCodeAndFoodId(updateReq.getTableCode(), updateReq.getFoodId());
        if (cartItem == null) {
            ArgumentAssert.error(SystemErrorCode.DATA_NOT_EXIST);
        }
        cartItem.setFoodQuantity(updateReq.getFoodQuantity());
        cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addCartItem(CartItem cartItem) {
        // Fetch all cart items for the given table code
        String tableCode = cartItem.getTableCode();
        Long foodId = cartItem.getFoodId();
        CartItem existingCartItem = cartItemRepository.getByTableCodeAndFoodId(tableCode, foodId);
        if (existingCartItem == null) {
            // If no existing cart items, save the new cart item
            cartItem.setId(null);
            cartItemRepository.save(cartItem);
        }
        else {
            // If there are existing cart items, update the latest first one
            existingCartItem.setFoodQuantity(existingCartItem.getFoodQuantity() + cartItem.getFoodQuantity());
            cartItemRepository.save(existingCartItem);
        }
    }

    @Override
    public void batchAddCartItem(List<CartItem> cartItems) {
        if (cartItems == null || cartItems.isEmpty()) {
            return;
        }
        cartItems.forEach(this::addCartItem);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCartItem(String tableCode, Long foodId) {
        int count = cartItemRepository.deleteByTableCodeAndFoodId(tableCode, foodId);
        return count > 0;
    }

    @Override
    protected JpaRepository<CartItem, Long> getBaseRepository() {
        return this.cartItemRepository;
    }

}
