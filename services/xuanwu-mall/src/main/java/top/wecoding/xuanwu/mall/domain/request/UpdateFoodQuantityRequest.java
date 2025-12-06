package top.wecoding.xuanwu.mall.domain.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wecoding
 * @since 0.9
 */
@Data
public class UpdateFoodQuantityRequest implements Serializable {

    private String tableCode;

    private Long foodId;

    private Long foodQuantity;

}
