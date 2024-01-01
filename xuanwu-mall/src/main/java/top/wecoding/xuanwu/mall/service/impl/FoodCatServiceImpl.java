package top.wecoding.xuanwu.mall.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import top.wecoding.xuanwu.mall.domain.entity.FoodCat;
import top.wecoding.xuanwu.mall.repository.FoodCatRepository;
import top.wecoding.xuanwu.mall.service.FoodCatService;
import top.wecoding.xuanwu.orm.service.BaseServiceImpl;

/**
 * 菜品分类 - ServiceImpl
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-19 21:19:14
 */
@Service
@RequiredArgsConstructor
public class FoodCatServiceImpl extends BaseServiceImpl<FoodCat, Long> implements FoodCatService {

	private final FoodCatRepository foodCatRepository;

	@Override
	protected JpaRepository<FoodCat, Long> getBaseRepository() {
		return this.foodCatRepository;
	}

}
