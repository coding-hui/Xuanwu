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
public class OrderInfoPageRequest implements Serializable {

	@Query
	private Long id;

	@Query(blurry = "tableCode")
	private String tableCode;

	@Query(blurry = "orderSn")
	private String orderSn;

	@Query
	private Integer payType;

	@Query
	private Integer sourceType;

	@Query
	private Integer status;

	@Query(propName = "status", type = Query.Type.IN)
	private List<Integer> statusList;

	@Query(blurry = "note")
	private String note;

}
