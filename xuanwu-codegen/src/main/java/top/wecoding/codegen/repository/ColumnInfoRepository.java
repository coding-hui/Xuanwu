package top.wecoding.codegen.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.wecoding.codegen.domain.entity.ColumnEntity;
import top.wecoding.xuanwu.orm.repository.LogicDeleteRepository;

/**
 * @author wecoding
 * @since 0.9
 */
public interface ColumnInfoRepository
		extends LogicDeleteRepository<ColumnEntity, Long>, JpaSpecificationExecutor<ColumnEntity> {

}
