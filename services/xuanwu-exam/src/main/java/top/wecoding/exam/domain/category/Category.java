package top.wecoding.exam.domain.category;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  private Long id;

  private String name;

  private Long parentId;

  private String path;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
