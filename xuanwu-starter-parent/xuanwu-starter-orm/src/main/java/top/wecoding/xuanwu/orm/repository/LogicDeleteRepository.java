package top.wecoding.xuanwu.orm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import top.wecoding.xuanwu.orm.domain.LogicDeleteEntity;

/**
 * @author wecoding
 * @since 0.9
 */
@NoRepositoryBean
public interface LogicDeleteRepository<T extends LogicDeleteEntity, PK>
    extends JpaRepository<T, PK> {}
