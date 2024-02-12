package top.wecoding.xuanwu.mall.domain.request;

import lombok.Data;
import top.wecoding.xuanwu.orm.annotation.Query;

import java.io.Serializable;
import java.util.List;

/**
 * @author wecoding
 * @since 0.9
 */
@Data
public class FoodInfoPageRequest implements Serializable {

	@Query
	private Long id;

	@Query(blurry = "name")
	private String name;

	@Query(blurry = "description")
	private String description;

	@Query(propName = "id", type = Query.Type.IN, joinName = "category")
	private List<String> categoryIds;

}
