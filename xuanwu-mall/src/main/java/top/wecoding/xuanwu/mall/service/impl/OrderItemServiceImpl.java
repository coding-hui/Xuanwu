package top.wecoding.xuanwu.mall.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import top.wecoding.xuanwu.mall.domain.entity.OrderItem;
import top.wecoding.xuanwu.mall.repository.OrderItemRepository;
import top.wecoding.xuanwu.mall.service.OrderItemService;
import top.wecoding.xuanwu.orm.service.BaseServiceImpl;

/**
 * 订单中所包含的商品 - ServiceImpl
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-30 11:29:53
 */
@Service
@RequiredArgsConstructor
public class OrderItemServiceImpl extends BaseServiceImpl<OrderItem, Long> implements OrderItemService {

	private final OrderItemRepository orderItemRepository;

	@Override
	protected JpaRepository<OrderItem, Long> getBaseRepository() {
		return this.orderItemRepository;
	}

}
