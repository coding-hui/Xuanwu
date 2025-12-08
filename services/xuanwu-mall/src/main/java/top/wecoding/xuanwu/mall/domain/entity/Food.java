package top.wecoding.xuanwu.mall.domain.entity;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import top.wecoding.xuanwu.orm.domain.BaseEntity;

/**
 * mall_food - 菜品
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-19 22:37:39
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mall_food")
@SuperBuilder(toBuilder = true)
public class Food extends BaseEntity implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  /** 菜品主键 */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** 菜品分类ID */
  @ManyToOne
  @JoinColumn(name = "category_id")
  private FoodCat category;

  /** 菜品名称 */
  @Column(name = "name", length = 64)
  private String name;

  /** 菜品描述 */
  @Column(name = "description")
  private String description;

  /** 菜品价格 */
  @Column(name = "price", precision = 10, scale = 2)
  private BigDecimal price;

  /** 最小订购数量 */
  @Column(name = "min_order_count")
  private Integer minOrderCount;

  /** 单位 */
  @Column(name = "unit")
  private String unit;

  /** 包装盒数量 */
  @Column(name = "box_num")
  private Integer boxNum;

  /** 包装盒价格 */
  @Column(name = "box_price", precision = 10, scale = 2)
  private BigDecimal boxPrice;

  /** 菜品上下架状态，0表上架，1表下架 */
  @Column(name = "is_sold_out")
  private Integer soldOut;

  /** 菜品图片 URL */
  @Column(name = "picture", length = 1024)
  private String picture;

  /** 菜品排序序号 */
  @Column(name = "sort")
  private Integer sort;

  @OneToMany(
      mappedBy = "food",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  private List<SkuStock> skus;
}
