package top.wecoding.xuanwu.mall.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import top.wecoding.xuanwu.mall.config.ConditionalOnFrontendEnabled;

/**
 * @author wecoding
 * @since 0.8
 */
@Controller
@ConditionalOnFrontendEnabled
public class FrontendForwardController {

  /**
   * forward
   *
   * @return forward to client {@code index.html}.
   */
  @GetMapping(value = "/")
  public String forward() {
    return "forward:index.html";
  }
}
