package top.wecoding.codegen.api.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wecoding.codegen.service.TableInfoService;
import top.wecoding.xuanwu.core.annotation.Version;
import top.wecoding.xuanwu.core.base.R;

/**
 * @author wecoding
 * @since 0.9
 */
@Version("v1")
@RequiredArgsConstructor
@RestController("tableController.v1")
@RequestMapping("/v1/tables")
public class TableController {

	private final TableInfoService tableInfoService;

	@GetMapping("/{tableId}")
	public R<?> tableInfo(@PathVariable("tableId") Long tableId) {
		return R.ok(tableId);
	}

	@GetMapping
	public R<?> listTables(@PageableDefault Pageable page) {
		return R.ok(page);
	}

}
