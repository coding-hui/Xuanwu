package top.wecoding.xuanwu.orm.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;

/**
 * @author wecoding
 * @since 0.9
 */
@Getter
@Setter
@SuperBuilder
@MappedSuperclass
public class LogicDeleteEntity extends BaseEntity {

  public static final String DELETE_FIELD = "is_deleted";

  public static final String SOFT_DELETE_WHERE = "is_deleted = 0";

  public static final String SOFT_DELETE_SET = "is_deleted = null";

  public static final String SOFT_DELETE_HQL_SET = "deleted = null";

  @JsonIgnore
  @Column(name = "is_deleted")
  private Boolean deleted;

  public LogicDeleteEntity() {
    this.deleted = Boolean.FALSE;
  }
}
