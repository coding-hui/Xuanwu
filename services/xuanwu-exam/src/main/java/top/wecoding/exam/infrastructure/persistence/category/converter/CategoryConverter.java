package top.wecoding.exam.infrastructure.persistence.category.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import top.wecoding.exam.domain.category.Category;
import top.wecoding.exam.infrastructure.persistence.category.po.CategoryPO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoryConverter {

  CategoryPO toPO(Category category);

  Category toDomain(CategoryPO categoryPO);
}
