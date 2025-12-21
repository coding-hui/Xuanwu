package top.wecoding.exam.api.controller.category;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.wecoding.exam.api.converter.CategoryAssembler;
import top.wecoding.exam.api.dto.category.CategoryResponse;
import top.wecoding.exam.api.dto.category.CreateCategoryRequest;
import top.wecoding.exam.api.dto.category.UpdateCategoryRequest;
import top.wecoding.exam.application.usecase.CategoryUseCase;
import top.wecoding.exam.domain.category.Category;
import top.wecoding.exam.domain.exception.ExamErrorCode;
import top.wecoding.xuanwu.core.base.R;
import top.wecoding.xuanwu.core.exception.BaseUncheckedException;

@RequiredArgsConstructor
@RestController("categoryController.v1")
@RequestMapping(value = "/categories")
public class CategoryController {

  private final CategoryUseCase categoryUseCase;

  private final CategoryAssembler categoryAssembler;

  @PostMapping(version = "1")
  public R<CategoryResponse> createCategory(@RequestBody @Validated CreateCategoryRequest req) {
    Category c = categoryAssembler.toDomain(req);
    return R.ok(categoryAssembler.toResponse(categoryUseCase.createCategory(c)));
  }

  @PutMapping(value = "/{id}", version = "1")
  public R<CategoryResponse> updateCategory(
      @PathVariable Long id, @RequestBody @Validated UpdateCategoryRequest req) {
    req.setId(id);
    Category c = categoryAssembler.toDomain(req);
    return R.ok(categoryAssembler.toResponse(categoryUseCase.updateCategory(c)));
  }

  @GetMapping(value = "/{id}", version = "1")
  public R<CategoryResponse> getCategory(@PathVariable Long id) {
    return categoryUseCase
        .getCategory(id)
        .map(c -> R.ok(categoryAssembler.toResponse(c)))
        .orElseThrow(() -> new BaseUncheckedException(ExamErrorCode.CATEGORY_NOT_FOUND));
  }

  @GetMapping(value = "/children", version = "1")
  public R<List<CategoryResponse>> getChildren(@RequestParam("parentId") Long parentId) {
    List<CategoryResponse> list =
        categoryUseCase.getChildren(parentId).stream()
            .map(categoryAssembler::toResponse)
            .collect(Collectors.toList());
    return R.ok(list);
  }

  @GetMapping(value = "/tree", version = "1")
  public R<List<CategoryResponse>> getTree(@RequestParam("rootId") Long rootId) {
    List<CategoryResponse> list =
        categoryUseCase.getTree(rootId).stream()
            .map(categoryAssembler::toResponse)
            .collect(Collectors.toList());
    return R.ok(list);
  }

  @DeleteMapping(value = "/{id}", version = "1")
  public R<Void> deleteCategory(@PathVariable Long id) {
    categoryUseCase.deleteCategory(id);
    return R.ok();
  }
}
