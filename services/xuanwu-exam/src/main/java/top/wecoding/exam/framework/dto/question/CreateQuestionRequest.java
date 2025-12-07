package top.wecoding.exam.framework.dto.question;

import lombok.Data;
import top.wecoding.exam.domain.question.QuestionType;

@Data
public class CreateQuestionRequest {

    private String content;

    private QuestionType type;

    private Integer difficulty;

    private Integer score;

}
