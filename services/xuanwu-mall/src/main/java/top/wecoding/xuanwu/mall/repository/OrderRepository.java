package top.wecoding.xuanwu.mall.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.wecoding.xuanwu.mall.domain.entity.Order;
import top.wecoding.xuanwu.orm.repository.LogicDeleteRepository;

import java.util.List;

/**
 * 订单表 - Repository
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-30 11:29:28
 */
public interface OrderRepository extends LogicDeleteRepository<Order, Long>, JpaSpecificationExecutor<Order> {

    List<Order> findByTableCodeAndStatus(String tableCode, Integer status);

    int countByTableCodeAndStatus(String tableCode, Integer status);

}
