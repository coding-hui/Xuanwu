package top.wecoding.xuanwu.codegen.api.controller.v1;

import lombok.RequiredArgsConstructor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

  @GetMapping("/{tableId}/preview")
  public R<?> previewCode(@PathVariable("tableId") Long tableId) {
    return R.ok(generatorService.preview(tableId));
  }

  @GetMapping("/{tableId}/file")
  public R<?> toFile(@PathVariable("tableId") Long tableId) {
    generatorService.generator(tableId);
    return R.ok();
  }

  @GetMapping("/{tableId}/zip")
  public void download(
      @PathVariable("tableId") Long tableId, HttpServletRequest req, HttpServletResponse res) {
    byte[] bytes = generatorService.download(tableId);
    DownloadUtil.download(req, res, "xuanwu.zip", bytes);
  }
}
