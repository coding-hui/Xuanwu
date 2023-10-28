package top.wecoding.xuanwu.jpa.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wecoding
 * @since 0.9
 */
@Getter
@Setter
@MappedSuperclass
public class LogicDeleteEntity extends BaseEntity {

	@JsonIgnore
	@Column(name = "is_deleted")
	private Boolean deleted;

	public LogicDeleteEntity() {
		this.deleted = Boolean.FALSE;
	}

}
