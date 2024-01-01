package top.wecoding.xuanwu.mall.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.wecoding.xuanwu.mall.domain.entity.FoodCat;
import top.wecoding.xuanwu.orm.repository.LogicDeleteRepository;

/**
 * 菜品分类 - Repository
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-19 21:19:14
 */
public interface FoodCatRepository extends LogicDeleteRepository<FoodCat, Long>, JpaSpecificationExecutor<FoodCat> {

}
