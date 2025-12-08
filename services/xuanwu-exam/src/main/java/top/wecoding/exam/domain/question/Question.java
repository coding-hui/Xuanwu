package top.wecoding.exam.domain.question;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Question Aggregate Root */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Question implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  /** Question ID */
  private Long id;

  /** Question Title */
  private String title;

  /** Question Content/Description */
  private String content;

  /** Question Type (e.g., Single Choice, Multiple Choice, True/False, Short Answer, Coding) */
  private QuestionType type;

  /** Difficulty Level (1-5) */
  private Integer difficulty;

  /** Score */
  private Integer score;

  /** Status: 1-Draft, 2-Published, 3-Archived */
  private QuestionStatus status;

  /** Type-specific Metadata (JSON) */
  private String metaInfo;

  /** Tags (Comma separated) */
  private String tags;

  /** Category ID */
  private Long categoryId;

  /** Creator ID */
  private Long creatorId;

  /** Creation Time */
  private LocalDateTime createdAt;

  /** Update Time */
  private LocalDateTime updatedAt;
}
