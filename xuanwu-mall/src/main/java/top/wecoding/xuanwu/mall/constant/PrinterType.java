package top.wecoding.xuanwu.mall.constant;

import lombok.Getter;

/**
 * @author wecoding
 * @since 0.9
 */
@Getter
public enum PrinterType {

	USB(0, "USB"), POS(1, "网口"), CLOUD(2, "云打印机");

	private final int code;

	private final String text;

	PrinterType(int code, String text) {
		this.code = code;
		this.text = text;
	}

	public boolean is(Integer code) {
		if (code == null) {
			return false;
		}
		return this.code == code;
	}

}
