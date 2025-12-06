package top.wecoding.xuanwu.core.version;

import org.springframework.boot.info.GitProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wecoding.xuanwu.core.base.R;

import java.util.Optional;

/**
 * @author wecoding
 * @since 0.8
 */
@RequestMapping("/version")
@RestController("versionController")
public class VersionController {

    private final Optional<GitProperties> gitProperties;

    public VersionController(Optional<GitProperties> gitProperties) {
        this.gitProperties = gitProperties;
    }

    @GetMapping
    public R<?> info() {
        return R.ok(gitProperties.orElse(null));
    }

}
