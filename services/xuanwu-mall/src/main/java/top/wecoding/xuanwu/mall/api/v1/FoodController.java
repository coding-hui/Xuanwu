package top.wecoding.xuanwu.mall.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wecoding.xuanwu.core.annotation.Version;
import top.wecoding.xuanwu.core.base.R;
import top.wecoding.xuanwu.core.exception.DateNotFoundException;
import top.wecoding.xuanwu.mall.domain.entity.Food;
import top.wecoding.xuanwu.mall.domain.entity.SkuStock;
import top.wecoding.xuanwu.mall.domain.request.CreateFoodRequest;
import top.wecoding.xuanwu.mall.domain.request.FoodInfoPageRequest;
import top.wecoding.xuanwu.mall.domain.request.UpdateFoodRequest;
import top.wecoding.xuanwu.mall.service.FoodService;

import java.util.Comparator;
import java.util.List;

/**
 * 菜品 - API Controller
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-19 22:37:39
 */
@Version("v1")
@RequiredArgsConstructor
@RestController("foodController.v1")
@RequestMapping("/foods")
public class FoodController {

    private final FoodService foodService;

    @GetMapping("/{id}")
    public R<Food> getInfo(@PathVariable("id") Long id) {
        Food foodInfo = foodService.getById(id).orElseThrow(DateNotFoundException::new);
        if (foodInfo.getSkus() != null) {
            List<SkuStock> skus = foodInfo.getSkus().stream().sorted(Comparator.comparing(SkuStock::getSort)).toList();
            foodInfo.setSkus(skus);
        }
        return R.ok(foodInfo);
    }

    @GetMapping("")
    public R<?> paging(@PageableDefault Pageable pageReq, FoodInfoPageRequest queryParams) {
        return R.ok(foodService.listFoods(queryParams, pageReq));
    }

    @PostMapping("")
    public R<Food> create(@RequestBody @Validated CreateFoodRequest createReq) {
        foodService.createFood(createReq);
        return R.ok();
    }

    @PutMapping("/{id}")
    public R<Food> update(@PathVariable("id") Long id, @RequestBody @Validated UpdateFoodRequest updateReq) {
        updateReq.setId(id);
        foodService.updateFood(updateReq);
        return R.ok();
    }

    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable("id") Long id) {
        foodService.deleteById(id);
        return R.ok();
    }

}
