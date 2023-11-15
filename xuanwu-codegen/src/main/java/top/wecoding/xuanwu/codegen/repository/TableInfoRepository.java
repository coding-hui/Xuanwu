package top.wecoding.xuanwu.codegen.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.wecoding.xuanwu.codegen.domain.entity.TableEntity;
import top.wecoding.xuanwu.orm.repository.LogicDeleteRepository;

/**
 * @author wecoding
 * @since 0.9
 */
public interface TableInfoRepository
		extends LogicDeleteRepository<TableEntity, Long>, JpaSpecificationExecutor<TableEntity> {

}
