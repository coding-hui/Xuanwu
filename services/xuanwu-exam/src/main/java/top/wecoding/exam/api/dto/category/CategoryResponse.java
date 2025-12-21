package top.wecoding.exam.api.dto.category;

import lombok.Data;

@Data
public class CategoryResponse {

  private Long id;

  private String name;

  private Long parentId;

  private String path;
}
