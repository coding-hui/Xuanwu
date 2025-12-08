package top.wecoding.exam.infrastructure.persistence.question.mapper;

import java.util.Optional;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import top.wecoding.exam.infrastructure.persistence.question.po.QuestionPO;

/** Question MyBatis Mapper */
@Mapper
public interface QuestionMapper {

  int insert(QuestionPO questionPO);

  int update(QuestionPO questionPO);

  Optional<QuestionPO> selectById(@Param("id") Long id);

  void deleteById(@Param("id") Long id);

  void deleteByIds(@Param("ids") Iterable<Long> ids);
}
