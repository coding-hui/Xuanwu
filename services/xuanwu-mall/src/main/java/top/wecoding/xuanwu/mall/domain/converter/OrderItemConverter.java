package top.wecoding.xuanwu.mall.domain.converter;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import top.wecoding.xuanwu.mall.domain.entity.CartItem;
import top.wecoding.xuanwu.mall.domain.entity.OrderItem;

/**
 * @author wecoding
 * @since 0.9
 */
@Mapper(componentModel = "spring")
public interface OrderItemConverter {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "deleted", ignore = true)
  @Mapping(target = "createdAt", ignore = true)
  @Mapping(target = "updatedAt", ignore = true)
  OrderItem cartItemToEntity(CartItem cartItem);
}
