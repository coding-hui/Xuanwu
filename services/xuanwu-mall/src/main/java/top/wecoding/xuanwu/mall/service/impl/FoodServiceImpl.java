package top.wecoding.xuanwu.mall.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.wecoding.xuanwu.core.base.PageResult;
import top.wecoding.xuanwu.mall.domain.converter.FoodConverter;
import top.wecoding.xuanwu.mall.domain.entity.Food;
import top.wecoding.xuanwu.mall.domain.entity.SkuStock;
import top.wecoding.xuanwu.mall.domain.request.CreateFoodRequest;
import top.wecoding.xuanwu.mall.domain.request.FoodInfoPageRequest;
import top.wecoding.xuanwu.mall.domain.request.UpdateFoodRequest;
import top.wecoding.xuanwu.mall.repository.FoodRepository;
import top.wecoding.xuanwu.mall.repository.SkuStockRepository;
import top.wecoding.xuanwu.mall.service.FoodService;
import top.wecoding.xuanwu.orm.helper.QueryHelp;
import top.wecoding.xuanwu.orm.service.BaseServiceImpl;

import java.util.List;

/**
 * 菜品 - ServiceImpl
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-19 22:37:39
 */
@Service
@RequiredArgsConstructor
public class FoodServiceImpl extends BaseServiceImpl<Food, Long> implements FoodService {

    private final FoodRepository foodRepository;

    private final SkuStockRepository skuStockRepository;

    private final FoodConverter foodConverter;

    @Override
    public PageResult<Food> listFoods(FoodInfoPageRequest queryParams, Pageable pageable) {
        Page<Food> pageResult = this.foodRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, queryParams, criteriaBuilder),
                pageable);
        return PageResult.of(pageResult.getContent(), pageResult.getTotalElements());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createFood(CreateFoodRequest createReq) {
        Food food = foodConverter.createFoodRequestToEntity(createReq);
        // save food info
        this.foodRepository.save(food);
        // save skus
        if (!CollectionUtils.isEmpty(createReq.getSkus())) {
            List<SkuStock> skus = createReq.getSkus().stream().peek(s -> s.setFood(food)).toList();
            skuStockRepository.saveAll(skus);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateFood(UpdateFoodRequest updateReq) {
        Food food = foodConverter.updateFoodRequestToEntity(updateReq);
        // save food info
        this.foodRepository.save(food);
        // save skus
        if (!CollectionUtils.isEmpty(updateReq.getSkus())) {
            skuStockRepository.deleteByFoodId(food.getId());
            List<SkuStock> skus = updateReq.getSkus().stream().peek(s -> s.setFood(food)).toList();
            skuStockRepository.saveAll(skus);
        }
    }

    @Override
    protected JpaRepository<Food, Long> getBaseRepository() {
        return this.foodRepository;
    }

}
