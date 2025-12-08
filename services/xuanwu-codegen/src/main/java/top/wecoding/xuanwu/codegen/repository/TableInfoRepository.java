package top.wecoding.xuanwu.codegen.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import top.wecoding.xuanwu.codegen.domain.entity.TableEntity;
import top.wecoding.xuanwu.orm.repository.LogicDeleteRepository;

/**
 * @author wecoding
 * @since 0.9
 */
public interface TableInfoRepository
    extends LogicDeleteRepository<TableEntity, Long>, JpaSpecificationExecutor<TableEntity> {

  Optional<TableEntity> findByTableName(String tableName);

  @Query(
      nativeQuery = true,
      value =
          """
          SELECT
              table_name,
              engine AS db_engine,
              table_collation,
              table_comment,
              create_time AS create_at,
              update_time AS update_at
          FROM information_schema.tables
          WHERE table_schema = (SELECT database())
          AND table_name LIKE CONCAT('%', :tableName, '%')
          ORDER BY create_time DESC
          """)
  Page<Object> listDbTablesByName(@Param("tableName") String tableName, Pageable pageable);

  @Query(
      nativeQuery = true,
      value =
          """
          SELECT
              table_name,
              engine AS db_engine,
              table_collation,
              table_comment,
              create_time AS create_at,
              update_time AS update_at
          FROM information_schema.tables
          WHERE table_schema = (SELECT database())
          AND table_name IN :tableNames
          ORDER BY create_time DESC
          """)
  List<Object> listDbTablesByNames(@Param("tableNames") List<String> tableNames);

  @Query(
      nativeQuery = true,
      value =
          """
          SELECT
              table_name,
              engine AS db_engine,
              table_collation,
              table_comment,
              create_time AS create_at,
              update_time AS update_at
          FROM information_schema.tables
          WHERE table_schema = (SELECT database())
          ORDER BY create_time DESC
          """)
  Page<Object> listDbTables(Pageable pageable);
}
