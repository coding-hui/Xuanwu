package top.wecoding.xuanwu.codegen.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * @author wecoding
 * @since 0.9
 */
@Getter
@AllArgsConstructor
@Accessors(fluent = true)
public enum YesOrNo {

	YES("1"), NO("0");

	private final String code;

	public static YesOrNo of(Object code) {
		if (code == null) {
			return NO;
		}
		return YES.is(code) ? YES : NO;
	}

	public boolean is(Object code) {
		return this.code.equals(String.valueOf(code));
	}

}
