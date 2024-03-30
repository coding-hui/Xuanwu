package top.wecoding.xuanwu.mall.domain.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.wecoding.xuanwu.mall.domain.entity.SkuStock;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author wecoding
 * @since 0.9
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFoodRequest {

	private Long id;

	/** 菜品名称 */
	@NotBlank(message = "菜品名称不能为空")
	private String name;

	/** 菜品描述 */
	private String description;

	@NotNull(message = "菜品分类不能为空")
	private Long categoryId;

	/** 菜品价格 */
	@NotNull(message = "菜品价格不能为空")
	private BigDecimal price;

	/** 最小订购数量 */
	private Integer minOrderCount;

	/** 单位 */
	@NotBlank(message = "菜品单位不能为空")
	private String unit;

	/** 包装盒数量 */
	private Integer boxNum;

	/** 包装盒价格 */
	private BigDecimal boxPrice;

	/** 菜品上下架状态，0表上架，1表下架 */
	@NotNull(message = "菜品上下架状态不能为空")
	private Integer soldOut;

	/** 菜品图片 URL */
	private String picture;

	/** 菜品排序序号 */
	private Integer sort;

	private List<SkuStock> skus;

}
