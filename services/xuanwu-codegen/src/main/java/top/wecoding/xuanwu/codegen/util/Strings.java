package top.wecoding.xuanwu.codegen.util;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import top.wecoding.xuanwu.core.util.Convert;

/**
 * @author wecoding
 * @since 0.9
 */
public class Strings {

  private static final char SEPARATOR = '_';

  public static String getModuleName(String packageName) {
    int lastIndex = packageName.lastIndexOf(".");
    int nameLength = packageName.length();
    return StringUtils.substring(packageName, lastIndex + 1, nameLength);
  }

  public static String getBusinessName(String tableName) {
    int lastIndex = tableName.lastIndexOf("_");
    int nameLength = tableName.length();
    return StringUtils.substring(tableName, lastIndex + 1, nameLength);
  }

  public static String toClassName(String tableName, String tablePrefix, boolean autoRemovePrefix) {
    if (autoRemovePrefix && StringUtils.isNotEmpty(tablePrefix)) {
      String[] searchList = StringUtils.split(tablePrefix, ",");
      tableName = replaceFirst(tableName, searchList);
    }
    return toCamelCase(tableName);
  }

  public static String replaceFirst(String replacementm, String[] searchList) {
    String text = replacementm;
    for (String searchString : searchList) {
      if (replacementm.startsWith(searchString)) {
        text = replacementm.replaceFirst(searchString, "");
        break;
      }
    }
    return text;
  }

  public static String toCamelCase(String s) {
    if (s == null) {
      return null;
    }
    if (s.indexOf(SEPARATOR) == -1) {
      return s;
    }
    s = s.toLowerCase();
    StringBuilder sb = new StringBuilder(s.length());
    boolean upperCase = false;
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);

      if (c == SEPARATOR) {
        upperCase = true;
      } else if (upperCase) {
        sb.append(Character.toUpperCase(c));
        upperCase = false;
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }

  public static Integer getColumnLength(String columnType) {
    if (StringUtils.indexOf(columnType, "(") > 0) {
      String length = StringUtils.substringBetween(columnType, "(", ")");
      return Integer.valueOf(length);
    } else {
      return 0;
    }
  }

  public static String getDbType(String columnType) {
    if (StringUtils.indexOf(columnType, "(") > 0) {
      return StringUtils.substringBefore(columnType, "(");
    } else {
      return columnType;
    }
  }

  /**
   * 格式化文本, {} 表示占位符<br>
   * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
   * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
   * 例：<br>
   * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br>
   * 转义{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
   * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
   *
   * @param template 文本模板，被替换的部分用 {} 表示
   * @param params 参数值
   * @return 格式化后的文本
   */
  public static String format(String template, Object... params) {
    if (StringUtils.isBlank(template) || ObjectUtils.isEmpty(params)) {
      return template;
    }
    return StrFormatter.format(template, params);
  }

  public static class StrFormatter {

    public static final String EMPTY_JSON = "{}";

    public static final char C_BACKSLASH = '\\';

    public static final char C_DELIM_START = '{';

    public static final char C_DELIM_END = '}';

    /**
     * 格式化字符串<br>
     * 此方法只是简单将占位符 {} 按照顺序替换为参数<br>
     * 如果想输出 {} 使用 \\转义 { 即可，如果想输出 {} 之前的 \ 使用双转义符 \\\\ 即可<br>
     * 例：<br>
     * 通常使用：format("this is {} for {}", "a", "b") -> this is a for b<br>
     * 转义{}： format("this is \\{} for {}", "a", "b") -> this is \{} for a<br>
     * 转义\： format("this is \\\\{} for {}", "a", "b") -> this is \a for b<br>
     *
     * @param strPattern 字符串模板
     * @param argArray 参数列表
     * @return 结果
     */
    public static String format(final String strPattern, final Object... argArray) {
      if (StringUtils.isEmpty(strPattern) || ObjectUtils.isEmpty(argArray)) {
        return strPattern;
      }
      final int strPatternLength = strPattern.length();

      // 初始化定义好的长度以获得更好的性能
      StringBuilder sbuf = new StringBuilder(strPatternLength + 50);

      int handledPosition = 0;
      int delimIndex; // 占位符所在位置
      for (int argIndex = 0; argIndex < argArray.length; argIndex++) {
        delimIndex = strPattern.indexOf(EMPTY_JSON, handledPosition);
        if (delimIndex == -1) {
          if (handledPosition == 0) {
            return strPattern;
          } else { // 字符串模板剩余部分不再包含占位符，加入剩余部分后返回结果
            sbuf.append(strPattern, handledPosition, strPatternLength);
            return sbuf.toString();
          }
        } else {
          if (delimIndex > 0 && strPattern.charAt(delimIndex - 1) == C_BACKSLASH) {
            if (delimIndex > 1 && strPattern.charAt(delimIndex - 2) == C_BACKSLASH) {
              // 转义符之前还有一个转义符，占位符依旧有效
              sbuf.append(strPattern, handledPosition, delimIndex - 1);
              sbuf.append(Convert.utf8Str(argArray[argIndex]));
              handledPosition = delimIndex + 2;
            } else {
              // 占位符被转义
              argIndex--;
              sbuf.append(strPattern, handledPosition, delimIndex - 1);
              sbuf.append(C_DELIM_START);
              handledPosition = delimIndex + 1;
            }
          } else {
            // 正常占位符
            sbuf.append(strPattern, handledPosition, delimIndex);
            sbuf.append(Convert.utf8Str(argArray[argIndex]));
            handledPosition = delimIndex + 2;
          }
        }
      }
      // 加入最后一个占位符后所有的字符
      sbuf.append(strPattern, handledPosition, strPattern.length());

      return sbuf.toString();
    }
  }
}
