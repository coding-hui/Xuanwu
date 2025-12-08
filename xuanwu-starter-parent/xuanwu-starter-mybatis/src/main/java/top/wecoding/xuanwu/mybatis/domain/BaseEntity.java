package top.wecoding.xuanwu.mybatis.domain;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author wecoding
 * @since 0.9
 */
@Getter
@Setter
@SuperBuilder(toBuilder = true)
public class BaseEntity implements Serializable {

  public static final String CREATED_AT = "createdAt";

  public static final String UPDATED_AT = "updatedAt";

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime createdAt;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime updatedAt;

  public BaseEntity() {}
}
