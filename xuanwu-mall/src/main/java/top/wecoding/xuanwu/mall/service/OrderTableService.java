package top.wecoding.xuanwu.mall.service;

import org.springframework.data.domain.Pageable;
import top.wecoding.xuanwu.core.base.PageResult;
import top.wecoding.xuanwu.mall.domain.entity.OrderTable;
import top.wecoding.xuanwu.mall.domain.response.OrderTableInfo;
import top.wecoding.xuanwu.orm.service.BaseService;

/**
 * 订单桌号 - Service
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-29 20:43:29
 */
public interface OrderTableService extends BaseService<OrderTable, Long> {

	PageResult<OrderTableInfo> listOrderTables(OrderTable params, Pageable pageable);

	void updateStatusByCode(String tableCode, Integer status);

	void completedOrderTable(Long id);

}
