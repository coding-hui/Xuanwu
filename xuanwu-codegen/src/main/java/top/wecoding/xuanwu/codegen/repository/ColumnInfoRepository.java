package top.wecoding.xuanwu.codegen.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.wecoding.xuanwu.codegen.domain.entity.ColumnEntity;
import top.wecoding.xuanwu.orm.repository.LogicDeleteRepository;

/**
 * @author wecoding
 * @since 0.9
 */
public interface ColumnInfoRepository
		extends LogicDeleteRepository<ColumnEntity, Long>, JpaSpecificationExecutor<ColumnEntity> {

}
