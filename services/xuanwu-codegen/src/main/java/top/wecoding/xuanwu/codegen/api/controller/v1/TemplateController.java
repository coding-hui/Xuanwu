package top.wecoding.xuanwu.codegen.api.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wecoding.xuanwu.codegen.service.TemplateFactory;
import top.wecoding.xuanwu.core.annotation.Version;
import top.wecoding.xuanwu.core.base.R;

/**
 * @author wecoding
 * @since 0.9
 */
@Version("v1")
@RequiredArgsConstructor
@RestController("templateController.v1")
@RequestMapping("/templates")
public class TemplateController {

    private final TemplateFactory templateFactory;

    @GetMapping("")
    public R<?> getAllTemplates() {
        return R.ok(templateFactory.getAllTemplates());
    }

}
