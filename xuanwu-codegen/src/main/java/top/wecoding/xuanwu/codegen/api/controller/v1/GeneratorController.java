package top.wecoding.xuanwu.codegen.api.controller.v1;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wecoding.xuanwu.codegen.service.GeneratorService;
import top.wecoding.xuanwu.codegen.util.DownloadUtil;
import top.wecoding.xuanwu.core.annotation.Version;
import top.wecoding.xuanwu.core.base.R;

/**
 * @author wecoding
 * @since 0.9
 */
@Version("v1")
@RequiredArgsConstructor
@RestController("generatorController.v1")
@RequestMapping("/generator")
public class GeneratorController {

	private final GeneratorService generatorService;

	@GetMapping("/{tableIdOrName}/preview")
	public R<?> previewCode(@PathVariable("tableIdOrName") String tableIdOrName) {
		return R.ok(generatorService.preview(tableIdOrName));
	}

	@GetMapping("/{tableIdOrName}/to_file")
	public R<?> toFile(@PathVariable("tableIdOrName") String tableIdOrName) {
		generatorService.generator(tableIdOrName);
		return R.ok();
	}

	@GetMapping("/{tableIdOrName}/download")
	public void download(@PathVariable("tableIdOrName") String tableIdOrName, HttpServletRequest req,
			HttpServletResponse res) {
		byte[] bytes = generatorService.download(tableIdOrName);
		DownloadUtil.download(req, res, "xuanwu.zip", bytes);
	}

}
