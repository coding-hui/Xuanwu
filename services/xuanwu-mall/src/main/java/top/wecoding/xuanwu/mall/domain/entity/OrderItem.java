package top.wecoding.xuanwu.mall.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.wecoding.xuanwu.orm.domain.LogicDeleteEntity;

/**
 * mall_order_item - 订单中所包含的商品
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-30 11:29:53
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "mall_order_item")
public class OrderItem extends LogicDeleteEntity implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** 订单id */
  @Column(name = "order_id")
  private Long orderId;

  @Column(name = "table_code")
  private String tableCode;

  /** 订单编号 */
  private String orderSn;

  private Long foodId;

  private String foodPic;

  private String foodName;

  private String foodSn;

  /** 销售价格 */
  private BigDecimal foodPrice;

  /** 购买数量 */
  private Integer foodQuantity;

  /** 商品sku编号 */
  private Integer foodSkuId;

  /** 商品sku条码 */
  private String foodSkuCode;

  /** 商品分类id */
  private Integer foodCategoryId;

  private Integer giftIntegration;

  private Integer giftGrowth;

  /** 商品促销分解金额 */
  private BigDecimal promotionAmount;

  /** 优惠券优惠分解金额 */
  private BigDecimal couponAmount;

  /** 积分优惠分解金额 */
  private BigDecimal integrationAmount;

  /** 该商品经过优惠后的分解金额 */
  private BigDecimal realAmount;

  /** 商品销售属性:[{"key":"颜色","value":"颜色"},{"key":"容量","value":"4G"}] */
  private String productAttr;
}
