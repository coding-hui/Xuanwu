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
public enum GeneratorType {

	ZIP, DOWNLOAD;

	public static GeneratorType of(Object code) {
		if (code == null) {
			return ZIP;
		}
		return DOWNLOAD.is(code) ? DOWNLOAD : ZIP;
	}

	public boolean is(Object code) {
		return this.name().equalsIgnoreCase(String.valueOf(code));
	}

}
