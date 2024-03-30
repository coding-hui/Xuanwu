package top.wecoding.xuanwu.codegen.api.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.wecoding.xuanwu.codegen.domain.entity.TableEntity;
import top.wecoding.xuanwu.codegen.service.TableInfoService;
import top.wecoding.xuanwu.core.annotation.Version;
import top.wecoding.xuanwu.core.base.R;

import java.util.Collections;
import java.util.List;

/**
 * @author wecoding
 * @since 0.9
 */
@Version("v1")
@RequiredArgsConstructor
@RestController("tableController.v1")
@RequestMapping("/tables")
public class TableController {

	private final TableInfoService tableInfoService;

	@GetMapping("/{tableId}")
	public R<?> tableInfo(@PathVariable("tableId") Long tableId) {
		return R.ok(tableInfoService.getTableInfo(tableId));
	}

	@GetMapping("/from_db")
	public R<?> listDbTables(@PageableDefault Pageable pageReq,
			@RequestParam(value = "database", required = false) String db,
			@RequestParam(value = "tableName", required = false) String tableName) {
		return R.ok(tableInfoService.listDbTables(db, tableName, pageReq));
	}

	@GetMapping("")
	public R<?> listTables(@PageableDefault Pageable pageReq,
			@RequestParam(value = "tableName", required = false) String tableName) {
		return R.ok(tableInfoService.listTables(tableName, pageReq));
	}

	@PostMapping
	public R<TableEntity> createTable(@RequestBody @Validated TableEntity tableEntity) {
		return R.ok(tableInfoService.createTable(tableEntity));
	}

	@PutMapping("/{id}")
	public R<TableEntity> updateTable(@PathVariable("id") Long id, @RequestBody @Validated TableEntity tableEntity) {
		return R.ok(tableInfoService.updateTable(id, tableEntity));
	}

	@GetMapping("/{table_name}/columns")
	public R<?> listColumns(@PathVariable("table_name") String tableName) {
		return R.ok(tableInfoService.listDbTableColumnsByTableName(tableName));
	}

	@GetMapping("/{table_name}/import")
	public R<?> importTable(@PathVariable("table_name") String tableName) {
		List<TableEntity> tableEntities = tableInfoService.batchImportTableFromDb(List.of(tableName));
		return R.ok(tableEntities.stream().findFirst().orElse(null));
	}

	@GetMapping("/batch_import")
	public R<?> batchImportTable(@RequestParam("tableNames") List<String> tableNames) {
		return R.ok(tableInfoService.batchImportTableFromDb(tableNames));
	}

	@GetMapping("/{tableId}/sync_db")
	public R<?> syncFromDb(@PathVariable("tableId") Long tableId) {
		tableInfoService.syncTableFromDb(Collections.singletonList(tableId));
		return R.ok();
	}

	@GetMapping("/batch_sync_db")
	public R<?> batchSyncFromDb(@RequestParam("tableIds") List<Long> tableIds) {
		tableInfoService.syncTableFromDb(tableIds);
		return R.ok();
	}

	@DeleteMapping("/{tableId}")
	public R<?> delete(@PathVariable("tableId") Long tableId) {
		TableEntity tableInfo = tableInfoService.getTableInfo(tableId);
		tableInfoService.delete(tableInfo);
		return R.ok();
	}

	@DeleteMapping("/batch_delete")
	public R<?> batchDelete(@RequestParam("tableIds") List<Long> tableIds) {
		tableInfoService.batchDelete(tableIds);
		return R.ok();
	}

}
