package top.wecoding.xuanwu.mall.domain.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.wecoding.xuanwu.mall.domain.entity.Food;
import top.wecoding.xuanwu.mall.domain.request.CreateFoodRequest;
import top.wecoding.xuanwu.mall.domain.request.UpdateFoodRequest;

/**
 * @author wecoding
 * @since 0.9
 */
@Mapper(componentModel = "spring")
public interface FoodConverter {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "skus", ignore = true)
    @Mapping(target = "category.id", source = "categoryId")
    Food createFoodRequestToEntity(CreateFoodRequest createReq);

    @Mapping(target = "category.id", source = "categoryId")
    Food updateFoodRequestToEntity(UpdateFoodRequest updateReq);

}
