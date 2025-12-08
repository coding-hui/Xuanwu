package top.wecoding.xuanwu.printer.escpos.params;

import lombok.Data;

/**
 * 二维码配置参数
 *
 * @author wecoding
 * @since 0.9
 */
@Data
public class QrCode {

  /** 打印内容类型 */
  private int type;

  /** 对齐方式 居左、居中、居右 */
  private int format;

  /** 空行行数 */
  private int line;

  /** 二维码内容 */
  private String text;
}
