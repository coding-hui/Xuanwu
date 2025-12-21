package top.wecoding.exam.infrastructure.persistence.category;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import top.wecoding.exam.domain.category.Category;
import top.wecoding.exam.domain.category.CategoryRepository;
import top.wecoding.exam.infrastructure.persistence.category.converter.CategoryConverter;
import top.wecoding.exam.infrastructure.persistence.category.mapper.CategoryMapper;
import top.wecoding.exam.infrastructure.persistence.category.po.CategoryPO;
import top.wecoding.xuanwu.core.exception.ServerException;
import top.wecoding.xuanwu.core.exception.SystemErrorCode;

@Primary
@Repository
@RequiredArgsConstructor
public class MyBatisCategoryRepository implements CategoryRepository {

  private final CategoryMapper categoryMapper;

  private final CategoryConverter categoryConverter;

  @Override
  public Category save(Category category) {
    if (category.getId() != null) {
      return this.update(category);
    }
    CategoryPO po = categoryConverter.toPO(category);
    po.setCreatedAt(LocalDateTime.now());
    po.setUpdatedAt(LocalDateTime.now());
    int rows = categoryMapper.insert(po);
    if (rows == 0) {
      throw new ServerException(SystemErrorCode.DATABASE_ERROR);
    }
    category.setId(po.getId());
    return categoryConverter.toDomain(po);
  }

  @Override
  public Category update(Category category) {
    CategoryPO po = categoryConverter.toPO(category);
    po.setUpdatedAt(LocalDateTime.now());
    int rows = categoryMapper.update(po);
    if (rows == 0) {
      throw new ServerException(SystemErrorCode.DATABASE_ERROR);
    }
    return categoryConverter.toDomain(po);
  }

  @Override
  public Optional<Category> findById(Long id) {
    return categoryMapper.selectById(id).map(categoryConverter::toDomain);
  }

  @Override
  public void deleteById(Long id) {
    categoryMapper.deleteById(id);
  }

  @Override
  public List<Category> findChildren(Long parentId) {
    return categoryMapper.selectChildren(parentId).stream()
        .map(categoryConverter::toDomain)
        .collect(java.util.stream.Collectors.toList());
  }

  @Override
  public List<Category> findTree(Long rootId) {
    return categoryMapper.selectTree(rootId).stream()
        .map(categoryConverter::toDomain)
        .collect(java.util.stream.Collectors.toList());
  }
}
