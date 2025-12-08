package top.wecoding.xuanwu.mall.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wecoding
 * @since 0.9
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

  private Long orderId;

  private Integer payType;

  private String tableCode;

  private Boolean printSalesTicket;
}
