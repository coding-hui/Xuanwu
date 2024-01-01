package top.wecoding.xuanwu.mall.service;

import org.springframework.data.domain.Pageable;
import top.wecoding.xuanwu.core.base.PageResult;
import top.wecoding.xuanwu.mall.domain.entity.Food;
import top.wecoding.xuanwu.mall.domain.request.CreateFoodRequest;
import top.wecoding.xuanwu.mall.domain.request.FoodInfoPageRequest;
import top.wecoding.xuanwu.mall.domain.request.UpdateFoodRequest;
import top.wecoding.xuanwu.orm.service.BaseService;

/**
 * 菜品 - Service
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-19 22:37:39
 */
public interface FoodService extends BaseService<Food, Long> {

	PageResult<Food> listFoods(FoodInfoPageRequest queryParams, Pageable pageable);

	void createFood(CreateFoodRequest createReq);

	void updateFood(UpdateFoodRequest updateReq);

}
