package top.wecoding.xuanwu.mall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.wecoding.xuanwu.mall.domain.entity.Food;

/**
 * 菜品 - Repository
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-19 22:37:39
 */
public interface FoodRepository extends JpaRepository<Food, Long>, JpaSpecificationExecutor<Food> {}
