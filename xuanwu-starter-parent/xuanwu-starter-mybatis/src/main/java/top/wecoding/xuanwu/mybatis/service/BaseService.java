package top.wecoding.xuanwu.mybatis.service;

import java.util.List;
import java.util.Optional;
import top.wecoding.xuanwu.core.base.PageResult;

/**
 * @author wecoding
 * @since 0.9
 */
public interface BaseService<T, ID> {

  List<T> list();

  PageResult<T> page(int page, int size);

  PageResult<T> page(T record, int page, int size);

  Optional<T> getById(ID key);

  long count(T record);

  int create(T record);

  int updateById(ID key, T record);

  int deleteById(ID key);
}
