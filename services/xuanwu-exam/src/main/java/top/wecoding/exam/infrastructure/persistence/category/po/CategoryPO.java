package top.wecoding.exam.infrastructure.persistence.category.po;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class CategoryPO {

  private Long id;

  private String name;

  private Long parentId;

  private String path;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
