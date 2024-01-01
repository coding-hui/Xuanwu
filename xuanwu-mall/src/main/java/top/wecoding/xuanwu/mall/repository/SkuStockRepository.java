package top.wecoding.xuanwu.mall.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.wecoding.xuanwu.mall.domain.entity.SkuStock;
import top.wecoding.xuanwu.orm.repository.LogicDeleteRepository;

/**
 * 菜品 Sku - Repository
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-21 21:21:55
 */
public interface SkuStockRepository extends LogicDeleteRepository<SkuStock, Long>, JpaSpecificationExecutor<SkuStock> {

	void deleteByFoodId(Long foodId);

}
