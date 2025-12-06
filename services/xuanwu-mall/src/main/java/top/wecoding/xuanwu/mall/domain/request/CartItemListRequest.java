package top.wecoding.xuanwu.mall.domain.request;

import lombok.Data;
import top.wecoding.xuanwu.orm.annotation.Query;

import java.io.Serializable;

/**
 * @author wecoding
 * @since 0.9
 */
@Data
public class CartItemListRequest implements Serializable {

    @Query
    private Long id;

    @Query(blurry = "tableCode")
    private String tableCode;

    @Query
    private Integer status;

}
