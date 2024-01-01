package top.wecoding.xuanwu.printer;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import top.wecoding.xuanwu.printer.config.PrinterConfig;

/**
 * @author wecoding
 * @since 0.9
 */
@AutoConfiguration
@EnableConfigurationProperties(PrinterConfig.class)
public class PrinterAutoConfiguration {

}
