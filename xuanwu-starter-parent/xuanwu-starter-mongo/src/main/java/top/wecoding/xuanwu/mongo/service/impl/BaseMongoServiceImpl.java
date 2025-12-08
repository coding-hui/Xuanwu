package top.wecoding.xuanwu.mongo.service.impl;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import top.wecoding.xuanwu.mongo.annotation.QueryField;
import top.wecoding.xuanwu.mongo.service.BaseMongoService;

/**
 * Mongo 基类服务实现
 *
 * @author liuyuhui
 * @date 2022/02/19
 */
@Slf4j
public class BaseMongoServiceImpl<T> implements BaseMongoService<T> {

  @Resource
  @Qualifier("mongoTemplate")
  protected MongoTemplate mongoTemplate;

  @Override
  public T save(T entity) {
    return mongoTemplate.save(entity);
  }

  @Override
  public Collection<T> saveBatch(Collection<T> entityList) {
    return mongoTemplate.insertAll(entityList);
  }

  @Override
  public void removeById(Serializable id) {
    Query query = new Query();
    query.addCriteria(Criteria.where("id").is(id));
    mongoTemplate.remove(query, this.getEntityClass());
  }

  @Override
  public void removeById(T entity) {
    mongoTemplate.remove(entity);
  }

  @Override
  public void remove(Query query) {
    mongoTemplate.remove(query, this.getEntityClass());
  }

  @Override
  public void remove(T entity) {
    Query query = buildBaseQuery(entity);
    mongoTemplate.remove(query, getEntityClass());
  }

  @Override
  public void updateById(Serializable id, T entity) {
    Query query = new Query();
    query.addCriteria(Criteria.where("id").is(id));
    Update update = buildBaseUpdate(entity);
    update(query, update);
  }

  @Override
  public void update(Query updateQuery, T entity) {
    Update update = buildBaseUpdate(entity);
    update(updateQuery, update);
  }

  @Override
  public void update(Query updateQuery, Update update) {
    mongoTemplate.updateMulti(updateQuery, update, this.getEntityClass());
  }

  @Override
  public T getById(Serializable id) {
    return mongoTemplate.findById(id, this.getEntityClass());
  }

  @Override
  public List<T> listByIds(Collection<? extends Serializable> idList) {
    Query query = new Query();
    query.addCriteria(Criteria.where("id").in(idList));
    return mongoTemplate.find(query, this.getEntityClass());
  }

  @Override
  public T getOne(Query query) {
    return mongoTemplate.findOne(query, this.getEntityClass());
  }

  @Override
  public long count() {
    return mongoTemplate.count(new Query(), this.getEntityClass());
  }

  @Override
  public long count(Query query) {
    return mongoTemplate.count(query, this.getEntityClass());
  }

  @Override
  public List<T> list(Query query) {
    return mongoTemplate.find(query, this.getEntityClass());
  }

  @Override
  public List<T> list(T entity) {
    Query query = buildBaseQuery(entity);
    return mongoTemplate.find(query, this.getEntityClass());
  }

  @Override
  public List<T> list() {
    return mongoTemplate.findAll(this.getEntityClass());
  }

  @Override
  public Page<T> page(PageRequest pageRequest, Query query) {
    query = query == null ? new Query(Criteria.where("_id").exists(true)) : query;
    int total = (int) this.count(query);
    query.with(pageRequest);
    // 记录
    List<T> records = mongoTemplate.find(query, this.getEntityClass());
    return new PageImpl<>(records, pageRequest, total);
  }

  @Override
  public Page<T> page(PageRequest pageRequest, Object query) {
    return page(pageRequest, this.buildBaseQuery(query));
  }

  private Update buildBaseUpdate(T t) {
    Update update = new Update();

    Field[] fields = t.getClass().getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      try {
        Object value = field.get(t);
        if (value != null) {
          update.set(field.getName(), value);
        }
      } catch (Exception e) {
        log.warn("failed to build update field: {}", field.getName(), e);
      }
    }
    return update;
  }

  private Query buildBaseQuery(Object t) {
    Query query = new Query();

    Field[] fields = t.getClass().getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      try {
        Object value = field.get(t);
        if (value != null) {
          QueryField queryField = field.getAnnotation(QueryField.class);
          if (queryField != null) {
            query.addCriteria(queryField.type().buildCriteria(queryField, field, value));
          }
        }
      } catch (IllegalArgumentException | IllegalAccessException e) {
        log.warn("failed to build query field: {}", field.getName(), e);
      }
    }
    return query;
  }

  @SuppressWarnings("unchecked")
  private Class<T> getEntityClass() {
    return ((Class<T>)
        ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);
  }
}
