package top.wecoding.xuanwu.core.message;

import java.util.List;
import java.util.Locale;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @author wecoding
 * @since 0.9
 */
public class I18n {

  private static final IMessageSource DEFAULT_MESSAGE_SOURCE;

  private static final ReloadableResourceBundleMessageSource PARENT_MESSAGE_SOURCE;

  private static final List<String> basenames = List.of("classpath:i18n/errors/messages-common");

  static {
    PARENT_MESSAGE_SOURCE = new ReloadableResourceBundleMessageSource();
    PARENT_MESSAGE_SOURCE.setBasenames(getBasenames());
    PARENT_MESSAGE_SOURCE.setDefaultEncoding("UTF-8");
    IMessageSource defaultMessageSource = new IMessageSource() {};
    defaultMessageSource.setParent(PARENT_MESSAGE_SOURCE);
    DEFAULT_MESSAGE_SOURCE = defaultMessageSource;
  }

  private I18n() throws IllegalAccessException {
    throw new IllegalAccessException();
  }

  public static String[] getBasenames() {
    return ArrayUtils.toStringArray(basenames.toArray());
  }

  /** 覆盖默认资源文件位置 */
  public static void setBasenames(String... names) {
    PARENT_MESSAGE_SOURCE.setBasenames(names);
  }

  /** 添加资源文件位置 */
  public static void addBasenames(String... names) {
    PARENT_MESSAGE_SOURCE.addBasenames(names);
  }

  public static String getMessage(String code, String defaultMessage) {
    return DEFAULT_MESSAGE_SOURCE.resolveMessage(
        PARENT_MESSAGE_SOURCE, code, null, defaultMessage, LocaleContextHolder.getLocale());
  }

  public static String getMessage(String code, String defaultMessage, Locale locale) {
    return DEFAULT_MESSAGE_SOURCE.resolveMessage(
        PARENT_MESSAGE_SOURCE, code, null, defaultMessage, locale);
  }

  public static String getMessage(String code, Object[] args, String defaultMessage) {
    return DEFAULT_MESSAGE_SOURCE.resolveMessage(
        PARENT_MESSAGE_SOURCE, code, args, defaultMessage, LocaleContextHolder.getLocale());
  }

  public static String getMessage(
      String code, Object[] args, String defaultMessage, Locale locale) {
    return DEFAULT_MESSAGE_SOURCE.resolveMessage(
        PARENT_MESSAGE_SOURCE, code, args, defaultMessage, locale);
  }

  public static String getMessage(String code) {
    return DEFAULT_MESSAGE_SOURCE.resolveMessage(
        PARENT_MESSAGE_SOURCE, code, null, LocaleContextHolder.getLocale());
  }

  public static String getMessage(String code, Locale locale) {
    return DEFAULT_MESSAGE_SOURCE.resolveMessage(PARENT_MESSAGE_SOURCE, code, null, locale);
  }

  public static String getMessage(String code, Object[] args) {
    return DEFAULT_MESSAGE_SOURCE.resolveMessage(
        PARENT_MESSAGE_SOURCE, code, args, LocaleContextHolder.getLocale());
  }

  public static String getMessage(String code, Object[] args, Locale locale) {
    return DEFAULT_MESSAGE_SOURCE.resolveMessage(PARENT_MESSAGE_SOURCE, code, args, locale);
  }

  public static String getMessageLocal(String code) {
    return PARENT_MESSAGE_SOURCE.getMessage(code, null, LocaleContextHolder.getLocale());
  }

  public static String getMessageLocal(String code, Locale locale) {
    return PARENT_MESSAGE_SOURCE.getMessage(code, null, locale);
  }

  public static String getMessageLocal(String code, Object[] args) {
    return PARENT_MESSAGE_SOURCE.getMessage(code, args, LocaleContextHolder.getLocale());
  }

  public static String getMessageLocal(String code, Object[] args, Locale locale) {
    return PARENT_MESSAGE_SOURCE.getMessage(code, args, locale);
  }
}
