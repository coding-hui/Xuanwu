package top.wecoding.exam.framework.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wecoding.xuanwu.core.base.R;

@RequiredArgsConstructor
@RestController("Version")
@RequestMapping
public class Version {

    @GetMapping(value = "/ping")
    public R<String> ping() {
        return R.ok("pong");
    }

}
