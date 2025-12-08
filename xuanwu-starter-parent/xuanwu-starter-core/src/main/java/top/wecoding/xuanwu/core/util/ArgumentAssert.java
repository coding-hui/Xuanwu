package top.wecoding.xuanwu.core.util;

import static top.wecoding.xuanwu.core.exception.SystemErrorCode.PARAM_ERROR;

import java.util.Collection;
import java.util.Map;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import top.wecoding.xuanwu.core.exception.ErrorCode;
import top.wecoding.xuanwu.core.exception.IllegalParameterException;

/**
 * @author liuyuhui
 * @since 0.5
 */
public class ArgumentAssert {

  public static void error(ErrorCode supplier, Object... args) {
    throw new IllegalParameterException(supplier, args);
  }

  public static void isTrue(boolean expression, ErrorCode supplier, Object... args) {
    Assert.isTrue(
        expression,
        () -> {
          throw new IllegalParameterException(supplier, args);
        });
  }

  public static void isFalse(boolean expression, ErrorCode supplier, Object... args) {
    Assert.isTrue(
        !expression,
        () -> {
          throw new IllegalParameterException(supplier, args);
        });
  }

  public static void isNull(@Nullable Object object, ErrorCode supplier, Object... args) {
    Assert.isNull(
        object,
        () -> {
          throw new IllegalParameterException(supplier, args);
        });
  }

  public static void notNull(@Nullable Object object, ErrorCode supplier, Object... args) {
    Assert.notNull(
        object,
        () -> {
          throw new IllegalParameterException(supplier, args);
        });
  }

  public static void isBlank(@Nullable String str, ErrorCode supplier, Object... args) {
    Assert.hasText(
        str,
        () -> {
          throw new IllegalParameterException(supplier, args);
        });
  }

  public static void notBlank(@Nullable String str, ErrorCode supplier, Object... args) {
    Assert.isTrue(
        !StringUtils.hasText(str),
        () -> {
          throw new IllegalParameterException(supplier, args);
        });
  }

  public static void hasLength(@Nullable String text, ErrorCode supplier, Object... args) {
    Assert.hasLength(
        text,
        () -> {
          throw new IllegalParameterException(supplier, args);
        });
  }

  public static void doesNotContain(
      @Nullable String textToSearch, String substring, ErrorCode supplier, Object... args) {
    Assert.doesNotContain(
        textToSearch,
        substring,
        () -> {
          throw new IllegalParameterException(supplier, args);
        });
  }

  public static void notEmpty(@Nullable Object[] array, ErrorCode supplier, Object... args) {
    Assert.notEmpty(
        array,
        () -> {
          throw new IllegalParameterException(supplier, args);
        });
  }

  public static void notEmpty(@Nullable Collection<?> collection, Object... args) {
    notEmpty(collection, PARAM_ERROR, args);
  }

  public static void notEmpty(
      @Nullable Collection<?> collection, ErrorCode supplier, Object... args) {
    Assert.notEmpty(
        collection,
        () -> {
          throw new IllegalParameterException(supplier, args);
        });
  }

  public static void noNullElements(@Nullable Object[] array, ErrorCode supplier, Object... args) {
    Assert.noNullElements(
        array,
        () -> {
          throw new IllegalParameterException(supplier, args);
        });
  }

  public static void noNullElements(
      @Nullable Collection<?> collection, ErrorCode supplier, Object... args) {
    Assert.noNullElements(
        collection,
        () -> {
          throw new IllegalParameterException(supplier, args);
        });
  }

  public static void notEmpty(@Nullable Map<?, ?> map, ErrorCode supplier, Object... args) {
    Assert.notEmpty(
        map,
        () -> {
          throw new IllegalParameterException(supplier, args);
        });
  }
}
