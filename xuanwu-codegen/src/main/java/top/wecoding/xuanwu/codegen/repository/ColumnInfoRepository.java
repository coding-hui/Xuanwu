package top.wecoding.xuanwu.codegen.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import top.wecoding.xuanwu.codegen.domain.entity.ColumnEntity;
import top.wecoding.xuanwu.orm.repository.LogicDeleteRepository;

import java.util.List;

/**
 * @author wecoding
 * @since 0.9
 */
public interface ColumnInfoRepository
		extends LogicDeleteRepository<ColumnEntity, Long>, JpaSpecificationExecutor<ColumnEntity> {

	@Query(nativeQuery = true, value = """
			SELECT
			 	column_name,
			 	( IF ( is_nullable = 'no' && column_key != 'PRI', '1', NULL) ) AS is_required,
			 	( IF ( column_key = 'PRI', '1', '0' ) ) AS is_pk,
			 	ordinal_position AS sort,
			 	column_comment,
			 	( IF ( extra = 'auto_increment', '1', '0') ) AS is_increment,
			 	column_type
			FROM information_schema.columns
			WHERE table_name = ?1
			AND table_schema = (select database()) order by ordinal_position
			""")
	List<Object> listDbTableColumnsByTableName(String tableName);

}
