package top.wecoding.xuanwu.mybatis.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

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

    public BaseEntity() {
    }

}
