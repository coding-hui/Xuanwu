package top.wecoding.xuanwu.mall.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import top.wecoding.xuanwu.orm.domain.LogicDeleteEntity;

import java.io.Serial;
import java.io.Serializable;

/**
 * mall_sku_stock - 菜品 Sku
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-21 21:21:55
 */
@Getter
@Setter
@Entity
@Table(name = "mall_sku_stock")
public class SkuStock extends LogicDeleteEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/** 菜品 */
	@JsonIgnore
	@JoinColumn(name = "food_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Food food;

	/** sku编码 */
	private String skuCode;

	/** sku的规格 */
	private String spec;

	/** upc码 */
	private String upc;

	/** sku的价格，不能为负数，不能超过10个字 */
	private String price;

	/** 排序 */
	private Long sort;

	/** sku的库存数量，不能为负数，也不能为小数，传"*"表示表示库存无限 */
	private String stock;

	/**
	 * 详细的份量数字 1.填数量(对应weight_unit中 1，2) 2.无需再填数量(对应weight_unit中3) 用法： ① 0表示清空
	 * weight,weight_unit ② -1和null
	 * 表示不更新weight,weight_unit；-2表示weight_unit需设置【xx人份】，例如：weight=-2，weight_unit="6人份" ③
	 * 其他正常设置weight,weight_unit
	 */
	private Long weight;

	/**
	 * 重量单位或份量单位 1）取值为：克、千克、两、斤、磅、盎司、毫升、升、寸、厘米 2）取值为：个、串、枚、粒、
	 * 块、只、副、卷、片、贯、碗、杯、袋、瓶、盒、包、锅、罐、扎
	 * 3）取值为：1人份、2人份、3人份、4人份、5人份、6人份、7人份、8人份、9人份、10人份【注意：若传入xx人份时，weight必须设置为-2】，例如：weight=-2，weight_unit="6人份"
	 */
	private String weightUnit;

	@Override
	public String toString() {
		return "SkuStock{" + "id=" + id + ", skuCode='" + skuCode + '\'' + ", spec='" + spec + '\'' + ", upc='" + upc
				+ '\'' + ", price='" + price + '\'' + ", sort=" + sort + ", stock='" + stock + '\'' + ", weight="
				+ weight + ", weightUnit='" + weightUnit + '\'' + '}';
	}

}
