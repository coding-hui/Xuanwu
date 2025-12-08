package top.wecoding.xuanwu.core.base;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.util.Assert;

/**
 * @author liuyuhui
 * @date 2022/10/5
 */
public abstract class PageSerializable<T> implements Page<T>, Serializable {

  @Serial private static final long serialVersionUID = 1L;

  protected long total;

  protected List<T> records;

  public PageSerializable() {}

  public PageSerializable(List<T> list, long total) {
    this.records = list;
    this.total = total;
  }

  @Override
  public long getTotal() {
    return total;
  }

  @Override
  public List<T> getRecords() {
    return records;
  }

  @Override
  public abstract <U> PageSerializable<U> map(Function<? super T, ? extends U> converter);

  protected <U> List<U> getConvertedContent(Function<? super T, ? extends U> converter) {

    Assert.notNull(converter, "Function must not be null");

    return getRecords().stream().map(converter).collect(Collectors.toList());
  }
}
