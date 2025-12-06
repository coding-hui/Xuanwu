package top.wecoding.xuanwu.mall.domain.request;

import lombok.Data;
import top.wecoding.xuanwu.orm.annotation.Query;

import java.io.Serializable;

/**
 * @author wecoding
 * @since 0.9
 */
@Data
public class PrinterServicePageRequest implements Serializable {

    @Query
    private Long id;

    @Query(blurry = "name")
    private String name;

    @Query(blurry = "description")
    private String description;

}
