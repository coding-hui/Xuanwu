package top.wecoding.xuanwu.mall.constant;

import lombok.Getter;

/**
 * @author wecoding
 * @since 0.9
 */
@Getter
public enum OrderTableStatus {

    AVAILABLE(1), ORDERING(2), IN_USE(3);

    private final Integer status;

    OrderTableStatus(Integer status) {
        this.status = status;
    }

}
