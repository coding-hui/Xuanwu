package top.wecoding.xuanwu.printer.escpos.params;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 需要打印的参数
 *
 * @author wecoding
 * @since 0.9
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PosParam {

  /** 替换模板中占位符的参数 */
  private Map<String, Object> keys;

  /** 商品信息参数集合 */
  private List<Map<String, Object>> goods;
}
