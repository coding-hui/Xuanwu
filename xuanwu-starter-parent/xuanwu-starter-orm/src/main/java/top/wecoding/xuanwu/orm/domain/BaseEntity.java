package top.wecoding.xuanwu.orm.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
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
@SuperBuilder(toBuilder = true)
@EntityListeners({ AuditingEntityListener.class })
public class BaseEntity implements Serializable {

    public static final String CREATED_AT = "createdAt";

    public static final String UPDATED_AT = "updatedAt";

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreatedDate
    @Column(name = CREATED_AT, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = UPDATED_AT)
    private LocalDateTime updatedAt;

    public BaseEntity() {
    }

}
