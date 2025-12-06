package top.wecoding.xuanwu.orm.service;

import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

/**
 * @author wecoding
 * @since 0.9
 */
public interface BaseService<T, ID> {

    List<T> list();

    Page<T> pageAll(int page, int size);

    Page<T> page(T record, int page, int size);

    Optional<T> getById(ID key);

    long count(T record);

    T create(T record);

    void delete(T record);

    void deleteById(ID key);

    boolean existsWithId(ID key);

    T updateById(ID key, T record);

}
