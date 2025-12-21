package top.wecoding.exam.domain.category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository {

  Category save(Category category);

  Category update(Category category);

  Optional<Category> findById(Long id);

  void deleteById(Long id);

  List<Category> findChildren(Long parentId);

  List<Category> findTree(Long rootId);
}
