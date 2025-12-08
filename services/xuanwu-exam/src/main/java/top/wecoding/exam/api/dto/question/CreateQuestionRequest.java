package top.wecoding.exam.api.dto.question;

import lombok.Data;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import top.wecoding.exam.domain.question.QuestionType;

@Data
public class CreateQuestionRequest {

  @NotBlank(message = "{question.title.not_blank}")
  private String title;

  @NotBlank(message = "{question.content.not_blank}")
  private String content;

  @NotNull(message = "{question.type.not_null}")
  private QuestionType type;

  @NotNull(message = "{question.difficulty.not_null}")
  @Min(value = 1, message = "{question.difficulty.min}")
  @Max(value = 5, message = "{question.difficulty.max}")
  private Integer difficulty;

  @NotNull(message = "{question.score.not_null}")
  @Min(value = 0, message = "{question.score.min}")
  private Integer score;

  private String metaInfo;

  private String tags;

  @NotNull(message = "{question.category_id.not_null}")
  private Long categoryId;
}
