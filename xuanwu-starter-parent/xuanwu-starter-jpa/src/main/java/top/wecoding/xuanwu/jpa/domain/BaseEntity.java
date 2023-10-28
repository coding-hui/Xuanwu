package top.wecoding.xuanwu.jpa.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author wecoding
 * @since 0.9
 */

@Getter
@Setter
@MappedSuperclass
@EntityListeners({ AuditingEntityListener.class })
public class BaseEntity implements Serializable {

	public static final String CREATED_AT = "createdAt";

	public static final String UPDATED_AT = "updatedAt";

	@CreatedDate
	@JsonFormat
	@Column(name = CREATED_AT)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@JsonFormat
	@Column(name = UPDATED_AT)
	private LocalDateTime updatedAt;

}
