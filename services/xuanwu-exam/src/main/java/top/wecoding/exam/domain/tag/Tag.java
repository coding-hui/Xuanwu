package top.wecoding.exam.domain.tag;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tag implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  private Long id;

  private String name;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;
}
