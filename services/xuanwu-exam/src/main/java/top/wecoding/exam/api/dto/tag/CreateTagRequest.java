package top.wecoding.exam.api.dto.tag;

import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class CreateTagRequest {

  @NotBlank(message = "{tag.name.not_blank}")
  @Size(max = 100, message = "{tag.name.size}")
  private String name;
}
