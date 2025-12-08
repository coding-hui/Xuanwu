package top.wecoding.xuanwu.printer.escpos.params;

import lombok.Data;

/**
 * 文本配置参数
 *
 * @author wecoding
 * @since 0.9
 */
@Data
public class Text {

  /** 打印内容类型 */
  private int type;

  /** 对齐方式 居左、居中、居右 */
  private int format;

  /** 空行行数 */
  private int line;

  /** 打印文本内容 */
  private String text;

  /** 文本字体大小 */
  private int size;

  /** 文本是否加粗 */
  private boolean bold;

  /** 文本下划线 */
  private boolean underline;
}
