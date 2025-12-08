package top.wecoding.xuanwu.printer.escpos.params;

import java.util.List;

import lombok.Data;

import com.alibaba.fastjson.JSONObject;

/**
 * 模板配置参数
 *
 * @author wecoding
 * @since 0.9
 */
@Data
public class PosTpl {

  private List<JSONObject> header;

  private List<Goods> goods;

  private List<JSONObject> bill;

  private List<JSONObject> footer;

  public List<JSONObject> getHeader() {
    return header;
  }
}
