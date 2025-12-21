package top.wecoding.exam.api.dto.category;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class UpdateCategoryRequest {

  @NotNull(message = "{category.id.not_null}")
  private Long id;

  @NotBlank(message = "{category.name.not_blank}")
  @Size(max = 100, message = "{category.name.size}")
  private String name;

  private Long parentId;
}
