package top.wecoding.xuanwu.core.base;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.function.Function;

/**
 * @author liuyuhui
 * @date 2022/10/6
 */
public interface Page<T> {

    @JsonProperty("total")
    long getTotal();

    @JsonProperty("records")
    List<T> getRecords();

    <U> Page<U> map(Function<? super T, ? extends U> converter);

}
