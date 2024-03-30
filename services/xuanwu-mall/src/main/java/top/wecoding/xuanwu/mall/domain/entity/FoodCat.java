package top.wecoding.xuanwu.mall.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.wecoding.xuanwu.orm.domain.LogicDeleteEntity;

import java.io.Serial;
import java.io.Serializable;

/**
 * mall_food_cat - 菜品分类
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-19 21:19:14
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mall_food_cat")
@SuperBuilder(toBuilder = true)
public class FoodCat extends LogicDeleteEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 名称 */
	@Column(length = 64)
	@NotBlank(message = "分类名称不能为空")
	private String name;

	/** 备注 */
	private String description;

	/** 类型 */
	private Integer type;

	/** 排序 */
	private Integer sort;

}
