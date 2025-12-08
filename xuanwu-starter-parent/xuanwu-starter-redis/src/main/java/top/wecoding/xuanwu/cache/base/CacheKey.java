package top.wecoding.xuanwu.cache.base;

import java.time.Duration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.lang.NonNull;

/**
 * 缓存 Key 封装
 *
 * @author liuyuhui
 * @date 2022/6/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CacheKey {

  /** 缓存 Key */
  @NonNull private String key;

  /** 超时时间/秒 */
  private Duration expire;

  public CacheKey(@NonNull final String key) {
    this.key = key;
  }
}
