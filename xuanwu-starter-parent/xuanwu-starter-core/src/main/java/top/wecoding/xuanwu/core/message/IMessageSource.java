package top.wecoding.xuanwu.core.message;

import java.text.MessageFormat;
import java.util.Locale;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

/**
 * @author wecoding
 * @since 0.9
 */
public interface IMessageSource {

  Logger LOGGER = LoggerFactory.getLogger(IMessageSource.class);

  default void setParent(MessageSource messageSource) {}

  default String resolveMessage(
      ReloadableResourceBundleMessageSource parentMessageSource,
      String code,
      Object[] args,
      Locale locale) {
    return resolveMessage(parentMessageSource, code, args, null, locale);
  }

  default String resolveMessage(
      ReloadableResourceBundleMessageSource parentMessageSource,
      String code,
      Object[] args,
      String defaultMessage,
      Locale locale) {
    String desc = null;
    try {
      desc = parentMessageSource.getMessage(code, null, locale);
    } catch (NoSuchMessageException e) {
      LOGGER.warn("not found message for code: {}", code);
    }
    if (StringUtils.isBlank(desc) && StringUtils.isNotBlank(defaultMessage)) {
      desc = defaultMessage;
    }
    if (StringUtils.isNotBlank(desc) && ArrayUtils.isNotEmpty(args)) {
      desc = new MessageFormat(desc, locale).format(args);
    }
    if (StringUtils.isBlank(desc)) {
      desc = code;
    }
    LOGGER.debug("resolve message. code={}, message={}, language={}", code, desc, locale);
    return desc;
  }
}
