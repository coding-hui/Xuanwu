package top.wecoding.xuanwu.orm.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @author wecoding
 * @since 0.9
 */
@Getter
@Setter
@SuperBuilder
@MappedSuperclass
public class LogicDeleteEntity extends BaseEntity {

	@JsonIgnore
	@Column(name = "is_deleted")
	private Boolean deleted;

	public LogicDeleteEntity() {
		this.deleted = Boolean.FALSE;
	}

}
