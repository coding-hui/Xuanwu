package top.wecoding.xuanwu.mall.domain.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import top.wecoding.xuanwu.mall.domain.entity.Order;

/**
 * @author wecoding
 * @since 0.9
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderTableInfo implements Serializable {

  /** 主键 */
  private Long id;

  /** 编号 */
  private String code;

  /** 备注 */
  private String description;

  /** 空闲状态 */
  private Integer status;

  private Integer numberOfDiners;

  private Order order;
}
