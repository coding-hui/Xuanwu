package top.wecoding.exam.application.service;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import top.wecoding.exam.application.usecase.CategoryUseCase;
import top.wecoding.exam.domain.category.Category;
import top.wecoding.exam.domain.category.CategoryRepository;

@Service
@RequiredArgsConstructor
public class CategoryService implements CategoryUseCase {

  private final CategoryRepository categoryRepository;

  @Override
  public Category createCategory(Category category) {
    if (category.getPath() == null || category.getPath().isBlank()) {
      String name = category.getName();
      category.setPath(category.getParentId() == null ? "/" + name : null);
    }
    return categoryRepository.save(category);
  }

  @Override
  public Category updateCategory(Category category) {
    return categoryRepository.update(category);
  }

  @Override
  public Optional<Category> getCategory(Long id) {
    return categoryRepository.findById(id);
  }

  @Override
  public void deleteCategory(Long id) {
    categoryRepository.deleteById(id);
  }

  @Override
  public List<Category> getChildren(Long parentId) {
    return categoryRepository.findChildren(parentId);
  }

  @Override
  public List<Category> getTree(Long rootId) {
    return categoryRepository.findTree(rootId);
  }
}
