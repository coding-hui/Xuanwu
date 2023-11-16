package top.wecoding.xuanwu.codegen.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.wecoding.xuanwu.codegen.domain.entity.ColumnEntity;
import top.wecoding.xuanwu.codegen.domain.entity.TableEntity;
import top.wecoding.xuanwu.codegen.enums.YesOrNo;
import top.wecoding.xuanwu.codegen.repository.ColumnInfoRepository;
import top.wecoding.xuanwu.codegen.repository.TableInfoRepository;
import top.wecoding.xuanwu.codegen.service.TableInfoService;
import top.wecoding.xuanwu.codegen.service.TemplateFactory;
import top.wecoding.xuanwu.codegen.util.Strings;
import top.wecoding.xuanwu.core.base.PageResult;
import top.wecoding.xuanwu.core.exception.ServerException;
import top.wecoding.xuanwu.core.exception.SystemErrorCode;
import top.wecoding.xuanwu.core.util.ArgumentAssert;
import top.wecoding.xuanwu.core.util.Convert;
import top.wecoding.xuanwu.orm.service.BaseServiceImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toMap;
import static top.wecoding.xuanwu.core.exception.SystemErrorCode.FAILURE;
import static top.wecoding.xuanwu.core.exception.SystemErrorCode.PARAM_ERROR;

/**
 * @author wecoding
 * @since 0.9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TableInfoServiceImpl extends BaseServiceImpl<TableEntity, Long> implements TableInfoService {

	@Getter
	private final TableInfoRepository baseRepository;

	private final ColumnInfoRepository columnInfoRepository;

	private final TemplateFactory templateFactory;

	@PersistenceContext
	private EntityManager em;

	@Override
	public TableEntity getTableInfo(Long tableId) {
		Optional<TableEntity> info = baseRepository.findById(tableId);
		if (info.isEmpty()) {
			ArgumentAssert.error(SystemErrorCode.DATA_NOT_EXIST);
		}
		return info.get();
	}

	@Override
	public PageResult<TableEntity> listDbTables(String db, String tableName, Pageable pageReq) {
		Page<Object> result = StringUtils.isBlank(tableName) ? this.baseRepository.listDbTables(pageReq)
				: this.baseRepository.listDbTablesByName(tableName, pageReq);
		return PageResult.of(mappingTableEntity(result.getContent()), result.getTotalElements());
	}

	@Override
	public List<TableEntity> listDbTablesByNames(List<String> tableNames) {
		List<Object> result = this.baseRepository.listDbTablesByNames(tableNames);
		return mappingTableEntity(result);
	}

	public List<ColumnEntity> listDbTableColumnsByTableName(String tableName) {
		List<?> resultList = this.columnInfoRepository.listDbTableColumnsByTableName(tableName);
		return resultList.stream().map(res -> {
			Object[] arr = (Object[]) res;
			return ColumnEntity.builder()
				.columnName(arr[0].toString())
				.isRequired(YesOrNo.of(arr[1]).code())
				.isPk(YesOrNo.of(arr[2]).code())
				.sort(Convert.toInt(arr[3]))
				.columnComment(Convert.utf8Str(arr[4]))
				.isIncrement(YesOrNo.of(arr[5]).code())
				.columnType(Convert.utf8Str(arr[6]))
				.build();
		}).sorted(Comparator.comparing(ColumnEntity::getSort)).collect(Collectors.toList());
	}

	@Override
	public TableEntity createTable(TableEntity tableEntity) {
		return baseRepository.save(tableEntity);
	}

	@Override
	public TableEntity updateTable(Long tableId, TableEntity tableEntity) {
		Optional<TableEntity> oldInfo = baseRepository.findById(tableId);
		if (oldInfo.isEmpty()) {
			ArgumentAssert.error(SystemErrorCode.DATA_NOT_EXIST);
		}
		tableEntity.setId(tableId);
		return baseRepository.save(tableEntity);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public List<TableEntity> batchImportTableFromDb(List<String> tableNames) {
		List<TableEntity> tableEntities = this.listDbTablesByNames(tableNames);
		if (tableEntities.isEmpty()) {
			log.error("no tables are found in the database: [{}]", tableEntities);
			ArgumentAssert.error(PARAM_ERROR, Strings.format("找不到表格 {}，请检查表名称是否拼写错误", tableNames));
		}
		try {
			for (TableEntity table : tableEntities) {
				String tableName = table.getTableName();
				List<ColumnEntity> columnEntities = this.listDbTableColumnsByTableName(tableName);
				templateFactory.create(table.getTplCategory()).initTableConfig(table, columnEntities);
				this.baseRepository.save(table);
			}
		}
		catch (Exception e) {
			throw new ServerException(FAILURE, "导入表结构失败：" + e.getMessage());
		}
		return tableEntities;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void syncTableFromDb(Collection<Long> tableIds) {
		List<TableEntity> tables = baseRepository.findAllById(tableIds);
		if (tables.isEmpty()) {
			log.warn("no tables are found in the database: {}", tableIds);
			return;
		}
		for (TableEntity table : tables) {
			var oldColumns = table.getColumns();

			var oldColumnsMap = oldColumns.stream().collect(toMap(ColumnEntity::getColumnName, Function.identity()));

			var dbColumns = this.listDbTableColumnsByTableName(table.getTableName());

			ArgumentAssert.notEmpty(dbColumns, FAILURE, "同步数据失败，原表结构不存在");

			var dbTableColumnNames = dbColumns.stream().map(ColumnEntity::getColumnName).toList();

			// initialize table common config
			templateFactory.create(table.getTplCategory()).initTableConfig(table, dbColumns);

			for (ColumnEntity dbColumn : dbColumns) {
				// updated if old column
				if (oldColumnsMap.containsKey(dbColumn.getColumnName())) {
					ColumnEntity oldColumn = oldColumnsMap.get(dbColumn.getColumnName());
					dbColumn.setId(oldColumn.getId());
					if (dbColumn.isList()) {
						dbColumn.setQueryType(oldColumn.getQueryType());
					}
					if (StringUtils.isNotEmpty(oldColumn.getIsRequired()) && !dbColumn.isPk()
							&& (dbColumn.isInsert() || dbColumn.isEdit())
							&& ((dbColumn.isUsableColumn()) || (!dbColumn.isSuperColumn()))) {
						// 如果是(新增/修改&非主键/非忽略及父属性)，继续保留必填/显示类型选项
						dbColumn.setIsRequired(oldColumn.getIsRequired());
						dbColumn.setHtmlType(oldColumn.getHtmlType());
					}
					columnInfoRepository.save(dbColumn);
					continue;
				}
				// added if new column
				columnInfoRepository.save(dbColumn);
			}
			var delColumns = oldColumns.stream()
				.filter(column -> !dbTableColumnNames.contains(column.getColumnName()))
				.toList();
			if (!CollectionUtils.isEmpty(delColumns)) {
				columnInfoRepository.deleteAllInBatch(delColumns);
			}
		}
	}

	private List<TableEntity> mappingTableEntity(List<?> result) {
		List<TableEntity> tableInfos = new ArrayList<>();
		for (Object obj : result) {
			Object[] arr = (Object[]) obj;
			TableEntity tableInfo = TableEntity.builder()
				.tableName(Convert.utf8Str(arr[0]))
				.dbEngine(Convert.utf8Str(arr[1]))
				.tableCollation(Convert.utf8Str(arr[2]))
				.tableComment(Convert.utf8Str(arr[3]))
				.build();
			tableInfos.add(tableInfo);
		}
		return tableInfos;
	}

}
