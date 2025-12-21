package top.wecoding.exam.api.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import top.wecoding.exam.api.dto.category.CategoryResponse;
import top.wecoding.exam.api.dto.category.CreateCategoryRequest;
import top.wecoding.exam.api.dto.category.UpdateCategoryRequest;
import top.wecoding.exam.domain.category.Category;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryAssembler {

  Category toDomain(CreateCategoryRequest request);

  Category toDomain(UpdateCategoryRequest request);

  CategoryResponse toResponse(Category category);
}
