package top.wecoding.xuanwu.mall.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.wecoding.xuanwu.orm.domain.LogicDeleteEntity;

/**
 * mall_cart_item - 购物车表
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-30 12:20:12
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "mall_cart_item")
public class CartItem extends LogicDeleteEntity implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "订单桌号不能为空")
  @Column(name = "table_code")
  private String tableCode;

  @NotNull(message = "菜品不能为空")
  @Column(name = "food_id")
  private Long foodId;

  @Column(name = "food_sku_id")
  private Long foodSkuId;

  /** 购买数量 */
  @NotNull(message = "购买数量不能为空")
  @Column(name = "food_quantity")
  private Long foodQuantity;

  /** 添加到购物车的价格 */
  @Column(name = "food_price")
  private BigDecimal foodPrice;

  /** 商品主图 */
  @Column(name = "food_pic")
  private String foodPic;

  /** 商品名称 */
  @Column(name = "food_name")
  private String foodName;

  /** 商品副标题（卖点） */
  @Column(name = "food_sub_title")
  private String foodSubTitle;

  /** 商品sku条码 */
  @Column(name = "food_sku_code")
  private String foodSkuCode;

  /** 会员昵称 */
  @Column(name = "member_nickname")
  private String memberNickname;

  /** 是否删除 */
  @Column(name = "delete_status")
  private Long deleteStatus;

  /** 商品分类 */
  @Column(name = "food_category_id")
  private Long foodCategoryId;

  @Column(name = "food_sn")
  private String foodSn;

  /** 商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}] */
  @Column(name = "food_attr")
  private String foodAttr;
}
