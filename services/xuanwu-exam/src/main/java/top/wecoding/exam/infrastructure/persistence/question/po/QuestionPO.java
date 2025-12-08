package top.wecoding.exam.infrastructure.persistence.question.po;

import java.time.LocalDateTime;

import lombok.Data;

/** Question Persistent Object */
@Data
public class QuestionPO {

  private Long id;

  private String title;

  private String content;

  private String type;

  private Integer difficulty;

  private Integer score;

  private Integer status;

  private String metaInfo;

  private String tags;

  private Long categoryId;

  private Long creatorId;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
