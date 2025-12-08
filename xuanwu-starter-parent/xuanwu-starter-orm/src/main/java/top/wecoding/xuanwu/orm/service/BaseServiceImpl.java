package top.wecoding.xuanwu.orm.service;

import java.beans.PropertyDescriptor;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import top.wecoding.xuanwu.core.exception.IllegalParameterException;
import top.wecoding.xuanwu.core.exception.SystemErrorCode;

/**
 * @author wecoding
 * @since 0.9
 */
@Slf4j
public abstract class BaseServiceImpl<T, ID> implements BaseService<T, ID> {

  protected abstract JpaRepository<T, ID> getBaseRepository();

  @Override
  public List<T> list() {
    return this.getBaseRepository().findAll();
  }

  @Override
  public Page<T> pageAll(int page, int size) {
    PageRequest pageRequest = PageRequest.of(page, size);
    return this.getBaseRepository().findAll(pageRequest);
  }

  @Override
  public Page<T> page(T record, int page, int size) {
    Example<T> example = Example.of(record);
    PageRequest pageRequest = PageRequest.of(page, size);
    return this.getBaseRepository().findAll(example, pageRequest);
  }

  @Override
  public Optional<T> getById(ID key) {
    return this.getBaseRepository().findById(key);
  }

  @Override
  public long count(T record) {
    return this.getBaseRepository().count(Example.of(record));
  }

  @Override
  public T create(T record) {
    return this.getBaseRepository().save(record);
  }

  @Override
  public void delete(T record) {
    this.getBaseRepository().delete(record);
  }

  @Override
  public void deleteById(ID key) {
    this.getBaseRepository().deleteById(key);
  }

  @Override
  public boolean existsWithId(ID key) {
    return this.getBaseRepository().existsById(key);
  }

  @Override
  public T updateById(ID key, T record) {
    Optional<T> oldInfo = this.getBaseRepository().findById(key);
    if (oldInfo.isEmpty()) {
      throw new IllegalParameterException(SystemErrorCode.DATA_NOT_EXIST);
    }
    // 将 oldInfo 复制到 record 中为 null 的属性
    BeanUtils.copyProperties(oldInfo.get(), record, getNoNullProperties(record));
    return this.getBaseRepository().save(record);
  }

  protected String[] getNoNullProperties(Object o) {
    // 获取对象的bean的包装类
    BeanWrapper bean = new BeanWrapperImpl(o);
    // 获取属性（字段）的描述
    PropertyDescriptor[] descriptors = bean.getPropertyDescriptors();
    Set<String> set = new HashSet<>();
    for (PropertyDescriptor descriptor : descriptors) {
      Object value = bean.getPropertyValue(descriptor.getName());
      if (!Objects.equals(value, null)) set.add(descriptor.getName());
    }
    String[] result = new String[set.size()];
    return set.toArray(result);
  }
}
