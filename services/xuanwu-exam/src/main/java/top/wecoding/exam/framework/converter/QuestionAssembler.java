package top.wecoding.exam.framework.converter;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import top.wecoding.exam.domain.question.Question;
import top.wecoding.exam.domain.question.QuestionStatus;
import top.wecoding.exam.framework.dto.question.CreateQuestionRequest;
import top.wecoding.exam.framework.dto.question.QuestionResponse;

/**
 * Question Assembler
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface QuestionAssembler {

    Question toDomain(CreateQuestionRequest request);

    QuestionResponse toResponse(Question question);

    default Integer map(QuestionStatus status) {
        return status != null ? status.getCode() : null;
    }

    default QuestionStatus map(Integer code) {
        return QuestionStatus.of(code);
    }

}
