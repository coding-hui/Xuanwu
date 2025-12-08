package top.wecoding.xuanwu.mall.domain.entity;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import top.wecoding.xuanwu.orm.domain.LogicDeleteEntity;

/**
 * mall_order - 订单表
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-30 11:29:28
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mall_order")
public class Order extends LogicDeleteEntity implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  /** 订单ID */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String tableCode;

  /** 订单编号 */
  private String orderSn;

  /** 用户帐号 */
  private String memberUsername;

  /** 订单总金额 */
  private BigDecimal totalAmount;

  /** 应付金额（实际支付金额） */
  private BigDecimal payAmount;

  /** 运费金额 */
  private BigDecimal freightAmount;

  /** 促销优化金额（促销价、满减、阶梯价） */
  private BigDecimal promotionAmount;

  /** 积分抵扣金额 */
  private BigDecimal integrationAmount;

  /** 优惠券抵扣金额 */
  private BigDecimal couponAmount;

  /** 管理员后台调整订单使用的折扣金额 */
  private BigDecimal discountAmount;

  /** 支付方式：0->未支付；1->支付宝；2->微信 */
  private Integer payType;

  /** 订单来源：0->PC订单；1->app订单 */
  private Integer sourceType;

  /** 订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单 */
  private Integer status;

  /** 订单类型：0->正常订单；1->秒杀订单 */
  private Integer orderType;

  /** 订单备注 */
  private String note;

  /** 删除状态：0->未删除；1->已删除 */
  private Integer deleteStatus;

  /** 支付时间 */
  private Date paymentTime;

  private Integer peopleCount;
}
