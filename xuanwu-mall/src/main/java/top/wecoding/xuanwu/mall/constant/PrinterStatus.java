package top.wecoding.xuanwu.mall.constant;

import lombok.Getter;

/**
 * @author wecoding
 * @since 0.9
 */
@Getter
public enum PrinterStatus {

	AVAILABLE(1), DISABLED(2);

	private final Integer status;

	PrinterStatus(Integer status) {
		this.status = status;
	}

}
