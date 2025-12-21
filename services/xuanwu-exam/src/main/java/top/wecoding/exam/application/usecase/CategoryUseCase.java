package top.wecoding.exam.application.usecase;

import java.util.List;
import java.util.Optional;

import top.wecoding.exam.domain.category.Category;

public interface CategoryUseCase {

  Category createCategory(Category category);

  Category updateCategory(Category category);

  Optional<Category> getCategory(Long id);

  void deleteCategory(Long id);

  List<Category> getChildren(Long parentId);

  List<Category> getTree(Long rootId);
}
