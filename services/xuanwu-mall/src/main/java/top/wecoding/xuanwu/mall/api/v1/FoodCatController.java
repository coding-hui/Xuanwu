package top.wecoding.xuanwu.mall.api.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import top.wecoding.xuanwu.core.base.PageResult;
import top.wecoding.xuanwu.core.base.R;
import top.wecoding.xuanwu.core.exception.DateNotFoundException;
import top.wecoding.xuanwu.mall.domain.entity.FoodCat;
import top.wecoding.xuanwu.mall.service.FoodCatService;

/**
 * 菜品分类 - API Controller
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-19 21:19:14
 */
@Version("v1")
@RequiredArgsConstructor
@RestController("foodCatController.v1")
@RequestMapping("/food/cats")
public class FoodCatController {

    private final FoodCatService foodCatService;

    @GetMapping("/{id}")
    public R<FoodCat> getInfo(@PathVariable("id") Long id) {
        return R.ok(foodCatService.getById(id).orElseThrow(DateNotFoundException::new));
    }

    @GetMapping("")
    public R<?> paging(@PageableDefault Pageable pageReq, FoodCat foodCat) {
        Page<FoodCat> pageResult = foodCatService.page(foodCat, pageReq.getPageNumber(), pageReq.getPageSize());
        return R.ok(PageResult.of(pageResult.getContent(), pageResult.getTotalElements()));
    }

    @PostMapping("")
    public R<FoodCat> create(@RequestBody @Validated FoodCat foodCat) {
        return R.ok(foodCatService.create(foodCat));
    }

    @PutMapping("/{id}")
    public R<FoodCat> update(@PathVariable("id") Long id, @RequestBody @Validated FoodCat foodCat) {
        foodCat.setId(id);
        return R.ok(foodCatService.updateById(id, foodCat));
    }

    @DeleteMapping("/{id}")
    public R<?> delete(@PathVariable("id") Long id) {
        foodCatService.deleteById(id);
        return R.ok();
    }

}
