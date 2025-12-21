package top.wecoding.exam.infrastructure.persistence.tag.mapper;

import java.util.List;
import java.util.Optional;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.wecoding.exam.infrastructure.persistence.tag.po.TagPO;

@Mapper
public interface TagMapper {

  int insert(TagPO tagPO);

  int update(TagPO tagPO);

  Optional<TagPO> selectById(@Param("id") Long id);

  Optional<TagPO> selectByName(@Param("name") String name);

  void deleteById(@Param("id") Long id);

  List<TagPO> list(@Param("keyword") String keyword);
}
