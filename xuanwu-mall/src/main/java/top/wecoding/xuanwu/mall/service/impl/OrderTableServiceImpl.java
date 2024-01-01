package top.wecoding.xuanwu.mall.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.wecoding.xuanwu.core.base.PageResult;
import top.wecoding.xuanwu.core.exception.SystemErrorCode;
import top.wecoding.xuanwu.core.util.ArgumentAssert;
import top.wecoding.xuanwu.mall.constant.OrderStatus;
import top.wecoding.xuanwu.mall.domain.entity.OrderTable;
import top.wecoding.xuanwu.mall.domain.response.OrderTableInfo;
import top.wecoding.xuanwu.mall.repository.OrderRepository;
import top.wecoding.xuanwu.mall.repository.OrderTableRepository;
import top.wecoding.xuanwu.mall.service.OrderTableService;
import top.wecoding.xuanwu.orm.service.BaseServiceImpl;

import java.util.List;

/**
 * 订单桌号 - ServiceImpl
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-29 20:43:29
 */
@Service
@RequiredArgsConstructor
public class OrderTableServiceImpl extends BaseServiceImpl<OrderTable, Long> implements OrderTableService {

	private final OrderTableRepository orderTableRepository;

	private final OrderRepository orderRepository;

	@Override
	public PageResult<OrderTableInfo> listOrderTables(OrderTable params, Pageable pageable) {
		Page<OrderTable> pageResult = orderTableRepository.findAll(Example.of(params), pageable);
		List<OrderTable> orderTables = pageResult.getContent();
		List<OrderTableInfo> orderTableInfos = orderTables.stream().map(t -> {
			var orders = orderRepository.findByTableCodeAndStatus(t.getCode(), OrderStatus.PENDING_PAYMENT.getCode());
			var infoBuilder = OrderTableInfo.builder()
				.id(t.getId())
				.code(t.getCode())
				.status(t.getStatus())
				.description(t.getDescription())
				.numberOfDiners(t.getNumberOfDiners());
			if (!CollectionUtils.isEmpty(orders)) {
				infoBuilder.order(orders.get(0));
			}
			return infoBuilder.build();
		}).toList();
		return PageResult.of(orderTableInfos, pageResult.getTotalElements());
	}

	@Override
	public void updateStatusByCode(String tableCode, Integer status) {
		OrderTable orderTable = orderTableRepository.getByCode(tableCode);
		if (orderTable == null) {
			ArgumentAssert.error(SystemErrorCode.DATA_NOT_EXIST);
		}
		orderTable.setStatus(status);
		orderTableRepository.save(orderTable);
	}

	@Override
	protected JpaRepository<OrderTable, Long> getBaseRepository() {
		return this.orderTableRepository;
	}

}
