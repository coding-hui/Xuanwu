package top.wecoding.xuanwu.codegen.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import static java.util.stream.Collectors.toMap;
import static top.wecoding.xuanwu.core.exception.SystemErrorCode.FAILURE;
import static top.wecoding.xuanwu.core.exception.SystemErrorCode.PARAM_ERROR;

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
import top.wecoding.xuanwu.codegen.service.TemplateService;
import top.wecoding.xuanwu.codegen.util.Strings;
import top.wecoding.xuanwu.core.base.PageResult;
import top.wecoding.xuanwu.core.exception.IllegalParameterException;
import top.wecoding.xuanwu.core.exception.ServerException;
import top.wecoding.xuanwu.core.exception.SystemErrorCode;
import top.wecoding.xuanwu.core.util.ArgumentAssert;
import top.wecoding.xuanwu.core.util.Convert;
import top.wecoding.xuanwu.orm.service.BaseServiceImpl;

/**
 * @author wecoding
 * @since 0.9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TableInfoServiceImpl extends BaseServiceImpl<TableEntity, Long>
    implements TableInfoService {

  private static final DateTimeFormatter DATE_TIME_FORMATTER =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

  @Getter private final TableInfoRepository baseRepository;

  private final ColumnInfoRepository columnInfoRepository;

  private final TemplateFactory templateFactory;

  @Override
  public TableEntity getTableInfo(Long tableId) {
    return baseRepository
        .findById(tableId)
        .orElseThrow(() -> new IllegalParameterException(SystemErrorCode.DATA_NOT_EXIST));
  }

  @Override
  public PageResult<TableEntity> listTables(String tableName, Pageable page) {
    TableEntity tableReq = TableEntity.builder().tableName(tableName).build();
    Page<TableEntity> pageResult = this.page(tableReq, page.getPageNumber(), page.getPageSize());
    return PageResult.of(pageResult.getContent(), pageResult.getTotalElements());
  }

  @Override
  public PageResult<TableEntity> listDbTables(String db, String tableName, Pageable pageReq) {
    Page<Object> result =
        StringUtils.isBlank(tableName)
            ? this.baseRepository.listDbTables(pageReq)
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
    return resultList.stream()
        .filter(obj -> obj instanceof Object[])
        .map(obj -> (Object[]) obj)
        .map(this::createColumnEntity)
        .filter(Objects::nonNull)
        .sorted(Comparator.comparing(ColumnEntity::getSort))
        .collect(Collectors.toList());
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
        TemplateService templateService = templateFactory.create(table.getTplCategory());
        templateService.initTableConfig(table);
        templateService.initTableColumnConfig(table, columnEntities);
        table.setColumns(columnEntities);
        this.baseRepository.save(table);
      }
    } catch (Exception e) {
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

      var oldColumnsMap =
          oldColumns.stream().collect(toMap(ColumnEntity::getColumnName, Function.identity()));

      var dbColumns = this.listDbTableColumnsByTableName(table.getTableName());

      ArgumentAssert.notEmpty(dbColumns, FAILURE, "同步数据失败，原表结构不存在");

      var dbTableColumnNames = dbColumns.stream().map(ColumnEntity::getColumnName).toList();

      // initialize table common config
      templateFactory.create(table.getTplCategory()).initTableColumnConfig(table, dbColumns);

      for (ColumnEntity dbColumn : dbColumns) {
        // updated if old column
        if (oldColumnsMap.containsKey(dbColumn.getColumnName())) {
          ColumnEntity oldColumn = oldColumnsMap.get(dbColumn.getColumnName());
          oldColumn.setIsRequired(dbColumn.getIsRequired());
          oldColumn.setIsPk(dbColumn.getIsPk());
          oldColumn.setSort(dbColumn.getSort());
          oldColumn.setColumnComment(dbColumn.getColumnComment());
          oldColumn.setIsIncrement(dbColumn.getIsIncrement());
          oldColumn.setColumnType(dbColumn.getColumnType());
          columnInfoRepository.save(oldColumn);
          continue;
        }
        // added if new column
        columnInfoRepository.save(dbColumn);
      }
      var delColumns =
          oldColumns.stream()
              .filter(column -> !dbTableColumnNames.contains(column.getColumnName()))
              .toList();
      if (!CollectionUtils.isEmpty(delColumns)) {
        columnInfoRepository.deleteAllInBatch(delColumns);
      }
    }
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void batchDelete(List<Long> tableIds) {
    List<TableEntity> tables = baseRepository.findAllById(tableIds);
    tables.forEach(this::delete);
  }

  @SneakyThrows(Exception.class)
  private List<TableEntity> mappingTableEntity(List<?> result) {
    return result.stream()
        .filter(obj -> obj instanceof Object[])
        .map(obj -> (Object[]) obj)
        .map(this::createTableEntity)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
  }

  private ColumnEntity createColumnEntity(Object[] arr) {
    try {
      return ColumnEntity.builder()
          .columnName(selfGetVal(arr, 0))
          .isRequired(YesOrNo.of(selfGetVal(arr, 1)).code())
          .isPk(YesOrNo.of(selfGetVal(arr, 2)).code())
          .sort(Convert.toInt(selfGetVal(arr, 3)))
          .columnComment(selfGetVal(arr, 4))
          .isIncrement(YesOrNo.of(selfGetVal(arr, 5)).code())
          .columnType(selfGetVal(arr, 6))
          .build();
    } catch (Exception e) {
      // Log or handle any exception that might occur during entity creation
      return null;
    }
  }

  private TableEntity createTableEntity(Object[] arr) {
    try {
      return TableEntity.builder()
          .tableName(selfGetVal(arr, 0))
          .dbEngine(selfGetVal(arr, 1))
          .tableCollation(selfGetVal(arr, 2))
          .tableComment(selfGetVal(arr, 3))
          .createdAt(parseDateTime(selfGetVal(arr, 4)))
          .updatedAt(parseDateTime(selfGetVal(arr, 5)))
          .build();
    } catch (Exception e) {
      // Log or handle any exception that might occur during entity creation
      return null;
    }
  }

  private String selfGetVal(Object[] arr, int index) {
    return (index >= 0 && index < arr.length) ? Convert.utf8Str(arr[index]) : null;
  }

  private LocalDateTime parseDateTime(String dateTimeString) {
    try {
      return LocalDateTime.parse(Convert.toStr(dateTimeString), DATE_TIME_FORMATTER);
    } catch (DateTimeParseException | NullPointerException e) {
      // Log or handle the parsing exception
      return null;
    }
  }
}
