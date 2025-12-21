package top.wecoding.exam.api.dto.tag;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class UpdateTagRequest {

  @NotNull(message = "{tag.id.not_null}")
  private Long id;

  @NotBlank(message = "{tag.name.not_blank}")
  @Size(max = 100, message = "{tag.name.size}")
  private String name;
}
