package top.wecoding.xuanwu.printer.config;

import lombok.Data;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

/**
 * @author wecoding
 * @since 0.9
 */
@Data
@ConfigurationProperties(prefix = PrinterConfig.PREFIX)
public class PrinterConfig {

  public static final String PREFIX = "xuanwu.printer";

  private Resource kitchenTemplate;

  private Resource demoOrder;
}
