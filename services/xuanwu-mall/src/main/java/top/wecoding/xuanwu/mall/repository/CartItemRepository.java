package top.wecoding.xuanwu.mall.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.wecoding.xuanwu.mall.domain.entity.CartItem;
import top.wecoding.xuanwu.orm.repository.LogicDeleteRepository;

import java.util.List;

/**
 * 购物车表 - Repository
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-30 12:20:12
 */
public interface CartItemRepository extends LogicDeleteRepository<CartItem, Long>, JpaSpecificationExecutor<CartItem> {

	CartItem getByTableCodeAndFoodId(String tableCode, Long foodId);

	int deleteByTableCodeAndFoodId(String tableCode, Long foodId);

	List<CartItem> findByTableCode(String tableCode);

}
