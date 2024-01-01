package top.wecoding.xuanwu.mall.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.wecoding.xuanwu.mall.domain.entity.OrderTable;
import top.wecoding.xuanwu.orm.repository.LogicDeleteRepository;

/**
 * 订单桌号 - Repository
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-29 20:43:29
 */
public interface OrderTableRepository
		extends LogicDeleteRepository<OrderTable, Long>, JpaSpecificationExecutor<OrderTable> {

	boolean existsByCode(String code);

	OrderTable getByCode(String code);

}
