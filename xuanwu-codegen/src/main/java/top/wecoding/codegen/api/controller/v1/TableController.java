package top.wecoding.codegen.api.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.wecoding.codegen.domain.entity.TableEntity;
import top.wecoding.codegen.service.TableInfoService;
import top.wecoding.xuanwu.core.annotation.Version;
import top.wecoding.xuanwu.core.base.R;

import java.util.List;

/**
 * @author wecoding
 * @since 0.9
 */
@Version("v1")
@RequiredArgsConstructor
@RestController("tableController.v1")
@RequestMapping("/api/v1/tables")
public class TableController {

	private final TableInfoService tableInfoService;

	@GetMapping("/{tableId}")
	public R<?> tableInfo(@PathVariable("tableId") Long tableId) {
		return R.ok(tableInfoService.getTableInfo(tableId));
	}

	@GetMapping
	public R<?> listTables(@PageableDefault Pageable page,
			@RequestParam(value = "database", required = false) String db,
			@RequestParam(value = "tableName", required = false) String tableName) {
		return R.ok(tableInfoService.listDbTables(db, tableName, page));
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

	@GetMapping("sync_db")
	public R<?> syncFromDb(@RequestParam("tableIds") List<Long> tableIds) {
		tableInfoService.syncTableFromDb(tableIds);
		return R.ok();
	}

}
