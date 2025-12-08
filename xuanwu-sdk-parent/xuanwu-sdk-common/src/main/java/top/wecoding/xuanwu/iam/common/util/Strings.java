package top.wecoding.xuanwu.iam.common.util;

import org.springframework.util.StringUtils;

/**
 * @author wecoding
 * @since 0.8
 */
public class Strings extends StringUtils {

  public static String clean(String s) {
    if (s == null) {
      return null;
    }
    String value = s.strip();
    if (value == null || value.isEmpty()) {
      return null;
    }
    return value;
  }
}
