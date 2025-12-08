package top.wecoding.exam.api.dto.question;

import lombok.Data;
import top.wecoding.exam.domain.question.QuestionType;

@Data
public class QuestionResponse {

  private Long id;

  private String title;

  private String content;

  private QuestionType type;

  private Integer difficulty;

  private Integer score;

  private Integer status;

  private String metaInfo;

  private String tags;

  private Long categoryId;
}
