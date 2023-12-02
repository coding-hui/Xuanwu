package top.wecoding.xuanwu.codegen.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author wecoding
 * @since 0.8
 */
@Controller
public class FrontendForwardController {

	/**
	 * forward
	 * @return forward to client {@code index.html}.
	 */
	@GetMapping(value = "/")
	public String forward() {
		return "forward:index.html";
	}

}
