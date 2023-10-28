package top.wecoding.xuanwu.jpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @author wecoding
 * @since 0.9
 */
@NoRepositoryBean
public interface LogicDeleteRepository<T, PK> extends JpaRepository<T, PK> {

}
