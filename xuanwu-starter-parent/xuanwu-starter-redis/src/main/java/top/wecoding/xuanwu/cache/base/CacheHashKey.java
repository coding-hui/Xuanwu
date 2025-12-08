package top.wecoding.xuanwu.cache.base;

import java.time.Duration;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

/**
 * @author liuyuhui
 * @date 2022/9/11
 */
@Getter
@NoArgsConstructor
public class CacheHashKey extends CacheKey {

  /** redis hash field */
  private Object field;

  public CacheHashKey(@NonNull String key, final Object field) {
    super(key);
    this.field = field;
  }

  public CacheHashKey(@NonNull String key, final Object field, Duration expire) {
    super(key, expire);
    this.field = field;
  }
}
