package top.wecoding.xuanwu.cache.base;

import static top.wecoding.xuanwu.core.constant.StrPool.COLON;
import static top.wecoding.xuanwu.core.constant.StrPool.STAR;
import static top.wecoding.xuanwu.core.exception.SystemErrorCode.CACHE_HASH_FIELD_CANNOT_BE_NOLL;
import static top.wecoding.xuanwu.core.exception.SystemErrorCode.CACHE_KEY_CANNOT_BE_NOLL;
import static top.wecoding.xuanwu.core.exception.SystemErrorCode.CACHE_PREFIX_CANNOT_BE_NOLL;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import top.wecoding.xuanwu.core.constant.StrPool;
import top.wecoding.xuanwu.core.util.ArgumentAssert;

/**
 * 缓存 key 构造器
 *
 * @author liuyuhui
 * @date 2022/9/11
 */
@FunctionalInterface
public interface CacheKeyBuilder {

  @NonNull
  String getPrefix();

  default String getTenant() {
    return StrPool.EMPTY;
  }

  @Nullable
  default Duration getExpire() {
    return null;
  }

  default String getPattern() {
    return String.format("*:%s:*", getPrefix());
  }

  default String getPattern(Object... suffix) {
    return getPattern(this.getTenant(), suffix);
  }

  default String getPattern(String tenant, Object... suffix) {
    String prefix = this.getPrefix();
    ArgumentAssert.notBlank(prefix, CACHE_PREFIX_CANNOT_BE_NOLL);

    tenant = StringUtils.hasText(tenant) ? STAR : tenant;

    List<String> regionList = new ArrayList<>();
    regionList.add(tenant);
    regionList.add(prefix);

    for (Object s : suffix) {
      regionList.add(ObjectUtils.isEmpty(s) ? STAR : String.valueOf(s));
    }
    return String.join(COLON, regionList);
  }

  default String getKey(Object... suffix) {
    ArrayList<String> regionList = new ArrayList<>();
    String tenant = this.getTenant();
    if (StringUtils.hasText(tenant)) {
      regionList.add(tenant);
    }

    String prefix = this.getPrefix();
    ArgumentAssert.notBlank(prefix, CACHE_PREFIX_CANNOT_BE_NOLL);

    regionList.add(prefix);

    for (Object s : suffix) {
      if (!ObjectUtils.isEmpty(s)) {
        regionList.add(String.valueOf(s));
      }
    }
    return String.join(COLON, regionList);
  }

  default CacheKey build(Object... suffix) {
    return new CacheKey(getKey(suffix), getExpire());
  }

  default CacheHashKey buildHashKey(@NonNull Object field, Object... suffix) {
    String key = getKey(suffix);

    ArgumentAssert.notBlank(key, CACHE_KEY_CANNOT_BE_NOLL);
    ArgumentAssert.notNull(field, CACHE_HASH_FIELD_CANNOT_BE_NOLL);

    return new CacheHashKey(key, field, getExpire());
  }

  default CacheHashKey buildHashKey(Object... suffix) {
    String key = getKey(suffix);

    ArgumentAssert.notBlank(key, CACHE_KEY_CANNOT_BE_NOLL);

    return new CacheHashKey(key, null, getExpire());
  }
}
