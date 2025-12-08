package top.wecoding.exam.infrastructure.persistence.question.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import top.wecoding.exam.domain.question.Question;
import top.wecoding.exam.domain.question.QuestionStatus;
import top.wecoding.exam.infrastructure.persistence.question.po.QuestionPO;

/** Question Converter */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface QuestionConverter {

  QuestionPO toPO(Question question);

  Question toDomain(QuestionPO questionPO);

  default Integer map(QuestionStatus status) {
    return status != null ? status.getCode() : null;
  }

  default QuestionStatus map(Integer code) {
    return QuestionStatus.of(code);
  }
}
