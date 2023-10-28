package top.wecoding.codegen.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import top.wecoding.codegen.domain.entity.ColumnEntity;
import top.wecoding.codegen.domain.entity.TableEntity;
import top.wecoding.codegen.enums.YesOrNo;
import top.wecoding.codegen.repository.ColumnInfoRepository;
import top.wecoding.codegen.repository.TableInfoRepository;
import top.wecoding.codegen.service.TableInfoService;
import top.wecoding.codegen.service.TemplateFactory;
import top.wecoding.xuanwu.core.base.PageResult;
import top.wecoding.xuanwu.core.exception.SystemErrorCode;
import top.wecoding.xuanwu.core.util.ArgumentAssert;
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
		String querySql = """
				SELECT
					table_name,
					engine AS db_engine,
					table_collation,
					table_comment,
					create_time AS create_at,
					update_time AS update_at
				FROM information_schema.tables
				WHERE table_schema = (SELECT database())
				AND table_name LIKE :table ORDER BY create_time DESC
				""";
		Query query = em.createNativeQuery(querySql);
		query.setFirstResult((pageReq.getPageNumber() - 1) * pageReq.getPageSize());
		query.setMaxResults(pageReq.getPageSize());
		query.setParameter("table", StringUtils.isNotBlank(tableName) ? ("%" + tableName + "%") : "%%");
		List<?> result = query.getResultList();
		List<TableEntity> tableInfos = new ArrayList<>();
		for (Object obj : result) {
			Object[] arr = (Object[]) obj;
			TableEntity tableInfo = TableEntity.builder()
				.tableName(ConvertUtils.convert(arr[0]))
				.dbEngine(ConvertUtils.convert(arr[1]))
				.tableCollation(ConvertUtils.convert(arr[2]))
				.tableComment(ConvertUtils.convert(arr[3]))
				.build();
			tableInfos.add(tableInfo);
		}
		String countSql = "SELECT count(1) FROM information_schema.tables WHERE table_schema = (SELECT database()) AND table_name LIKE :table";
		Query queryCount = em.createNativeQuery(countSql);
		queryCount.setParameter("table", StringUtils.isNotBlank(tableName) ? ("%" + tableName + "%") : "%%");
		Long totalElements = (Long) queryCount.getSingleResult();
		return PageResult.of(tableInfos, totalElements);
	}

	public List<ColumnEntity> listDbTableColumnsByTableName(String tableName) {
		String querySql = """
				SELECT
				 	column_name,
				 	( IF ( is_nullable = 'no' && column_key != 'PRI', '1', NULL) ) AS is_required,
				 	( IF ( column_key = 'PRI', '1', '0' ) ) AS is_pk,
				 	ordinal_position AS sort,
				 	column_comment,
				 	( IF ( extra = 'auto_increment', '1', '0') ) AS is_increment,
				 	column_type
				FROM information_schema.columns
				WHERE
					table_name = ?
					AND table_schema = (select database()) order by ordinal_position
				""";
		Query query = em.createNativeQuery(querySql);
		query.setParameter(1, tableName);
		List<?> resultList = query.getResultList();
		return resultList.stream().map(res -> {
			Object[] arr = (Object[]) res;
			return ColumnEntity.builder()
				.columnName(arr[0].toString())
				.isRequired(YesOrNo.of(arr[1]).code())
				.isPk(YesOrNo.of(arr[2]).code())
				.sort((Integer) ConvertUtils.convert(arr[3], Integer.class))
				.columnComment(ConvertUtils.convert(arr[4]))
				.isIncrement(YesOrNo.of(arr[5]).code())
				.columnType(ConvertUtils.convert(arr[6]))
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
	public void syncTableFromDb(Collection<Long> tableIds) {
		List<TableEntity> tables = baseRepository.findAllById(tableIds);
		if (tables.isEmpty()) {
			log.warn("no tables are found in the database: [{}]", tableIds);
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

}
