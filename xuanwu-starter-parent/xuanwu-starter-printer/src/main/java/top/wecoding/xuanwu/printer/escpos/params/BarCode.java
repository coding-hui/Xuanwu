package top.wecoding.xuanwu.printer.escpos.params;

import lombok.Data;

/**
 * 条形码配置参数
 *
 * @author wecoding
 */
@Data
public class BarCode {

	/**
	 * 打印内容类型
	 */
	private int type;

	/**
	 * 条形码数字
	 */
	private String text;

	/**
	 * 对齐方式 居左、居中、居右
	 */
	private int format;

	/**
	 * 空行行数
	 */
	private int line;

}
