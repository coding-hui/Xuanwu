package top.wecoding.codegen.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import top.wecoding.codegen.util.Strings;

import java.util.Map;
import java.util.Set;

/**
 * @author wecoding
 * @since 0.9
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = CodeGenProperties.PREFIX)
public class CodeGenProperties {

	public static final String PREFIX = "xuanwu.codegen";

	private String author;

	private String packageName;

	private boolean autoRemovePre;

	private String tablePrefix;

	private Map<String, Set<Template>> templates;

	@Data
	public static class Template {

		private String name;

		private String fileNameFormat;

		public String renderFileName(Object... args) {
			try {
				return Strings.format(fileNameFormat, args);
			}
			catch (Exception e) {
				log.error("failed to render tpl file name [{}]", name, e);
			}
			return null;
		}

	}

}
