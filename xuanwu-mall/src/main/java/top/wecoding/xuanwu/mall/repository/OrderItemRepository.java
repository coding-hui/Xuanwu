package top.wecoding.xuanwu.mall.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.wecoding.xuanwu.mall.domain.entity.OrderItem;
import top.wecoding.xuanwu.orm.repository.LogicDeleteRepository;

import java.util.List;

/**
 * 订单中所包含的商品 - Repository
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-30 11:29:53
 */
public interface OrderItemRepository
		extends LogicDeleteRepository<OrderItem, Long>, JpaSpecificationExecutor<OrderItem> {

	List<OrderItem> findByOrderId(Long orderId);

}
