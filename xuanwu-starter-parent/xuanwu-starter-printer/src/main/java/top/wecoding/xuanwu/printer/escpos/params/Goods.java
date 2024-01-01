package top.wecoding.xuanwu.printer.escpos.params;

import lombok.Data;

/**
 * 商品属性配置参数
 *
 * @author wecoding
 * @since 0.9
 */
@Data
public class Goods {

	/**
	 * 属性名称
	 */
	private String name;

	/**
	 * 对齐方式 居左、居中、居右
	 */
	private int format;

	/**
	 * 占半角字符宽度 58mm 每行32 80mm 每行48
	 */
	private int width;

	/**
	 * 占位符 格式${time}
	 */
	private String variable;

}
