package top.wecoding.exam.infrastructure.persistence.category.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.wecoding.exam.infrastructure.persistence.category.po.CategoryPO;

@Mapper
public interface CategoryMapper {

  int insert(CategoryPO categoryPO);

  int update(CategoryPO categoryPO);

  Optional<CategoryPO> selectById(@Param("id") Long id);

  void deleteById(@Param("id") Long id);

  List<CategoryPO> selectChildren(@Param("parentId") Long parentId);

  List<CategoryPO> selectTree(@Param("rootId") Long rootId);
}
