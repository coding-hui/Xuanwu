package top.wecoding.xuanwu.mall.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.wecoding.xuanwu.orm.domain.LogicDeleteEntity;

/**
 * mall_order_table - 订单桌号
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-29 20:43:29
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mall_order_table")
public class OrderTable extends LogicDeleteEntity implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  /** 主键 */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /** 编号 */
  @NotBlank(message = "桌号不能为空")
  private String code;

  /** 备注 */
  private String description;

  /** 空闲状态 */
  @Column(name = "status")
  private Integer status;

  @Column(name = "number_of_diners")
  private Integer numberOfDiners;
}
