package top.wecoding.xuanwu.codegen.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Pageable;
import top.wecoding.xuanwu.codegen.domain.entity.ColumnEntity;
import top.wecoding.xuanwu.codegen.domain.entity.TableEntity;
import top.wecoding.xuanwu.core.base.PageResult;
import top.wecoding.xuanwu.orm.service.BaseService;

/**
 * @author wecoding
 * @since 0.9
 */
public interface TableInfoService extends BaseService<TableEntity, Long> {

  TableEntity getTableInfo(Long tableId);

  PageResult<TableEntity> listTables(String tableName, Pageable page);

  PageResult<TableEntity> listDbTables(String db, String tableName, Pageable page);

  List<TableEntity> listDbTablesByNames(List<String> tableNames);

  List<ColumnEntity> listDbTableColumnsByTableName(String tableName);

  TableEntity createTable(TableEntity tableEntity);

  TableEntity updateTable(Long tableId, TableEntity tableEntity);

  List<TableEntity> batchImportTableFromDb(List<String> tableNames);

  void syncTableFromDb(Collection<Long> tables);

  void batchDelete(List<Long> tableIds);
}
