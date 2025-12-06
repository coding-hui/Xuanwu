package top.wecoding.xuanwu.mall.domain.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.wecoding.xuanwu.mall.domain.entity.Order;
import top.wecoding.xuanwu.mall.domain.entity.OrderItem;

import java.io.Serializable;
import java.util.List;

/**
 * @author wecoding
 * @since 0.9
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderResponse implements Serializable {

    private Order order;

    private List<OrderItem> orderItems;

}
