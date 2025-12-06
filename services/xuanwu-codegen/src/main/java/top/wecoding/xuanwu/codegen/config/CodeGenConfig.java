package top.wecoding.xuanwu.codegen.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;
import java.util.Set;

/**
 * @author wecoding
 * @since 0.9
 */
@Slf4j
@Data
@ConfigurationProperties(prefix = CodeGenConfig.PREFIX)
public class CodeGenConfig {

    public static final String PREFIX = "xuanwu.codegen";

    private String author;

    private String packageName;

    private boolean autoRemovePre;

    private String tablePrefix;

    private Map<String, Set<Template>> templates;

    private Frontend frontend;

    @Data
    public static class Frontend {

        public boolean enabled = true;

    }

    @Data
    public static class Template {

        private String name;

        private String fileNameFormat;

    }

}
