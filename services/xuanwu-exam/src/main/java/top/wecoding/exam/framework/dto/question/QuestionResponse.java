package top.wecoding.exam.framework.dto.question;

import top.wecoding.exam.domain.question.Question;
import top.wecoding.exam.domain.question.QuestionType;

import lombok.Data;

@Data
public class QuestionResponse {

    private Long id;

    private String content;

    private QuestionType type;

    private Integer difficulty;

    private Integer score;

    public static QuestionResponse from(Question q) {
        QuestionResponse r = new QuestionResponse();
        r.setId(q.getId());
        r.setContent(q.getContent());
        r.setType(q.getType());
        r.setDifficulty(q.getDifficulty());
        r.setScore(q.getScore());
        return r;
    }

}
