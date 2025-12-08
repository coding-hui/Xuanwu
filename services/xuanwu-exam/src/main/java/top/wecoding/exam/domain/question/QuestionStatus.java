package top.wecoding.exam.domain.question;

import java.util.Arrays;

import lombok.AllArgsConstructor;
import lombok.Getter;

/** Question Status Enum */
@Getter
@AllArgsConstructor
public enum QuestionStatus {
  DRAFT(1, "Draft"),
  PUBLISHED(2, "Published"),
  ARCHIVED(3, "Archived");

  private final Integer code;

  private final String description;

  public static QuestionStatus of(Integer code) {
    return Arrays.stream(values()).filter(s -> s.code.equals(code)).findFirst().orElse(null);
  }
}
