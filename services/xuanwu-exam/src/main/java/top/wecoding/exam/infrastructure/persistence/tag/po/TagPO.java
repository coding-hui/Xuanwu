package top.wecoding.exam.infrastructure.persistence.tag.po;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class TagPO {

  private Long id;

  private String name;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
