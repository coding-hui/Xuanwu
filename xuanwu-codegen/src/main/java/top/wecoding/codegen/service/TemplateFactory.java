package top.wecoding.codegen.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.wecoding.xuanwu.core.exception.SystemErrorCode;
import top.wecoding.xuanwu.core.util.ArgumentAssert;

import java.util.Map;
import java.util.Objects;

/**
 * @author wecoding
 * @since 0.9
 */
@Service
public class TemplateFactory {

	private final Map<String, TemplateService> creators;

	@Autowired
	public TemplateFactory(Map<String, TemplateService> creators) {
		this.creators = creators;
	}

	public TemplateService create(String template) {
		TemplateService service = null;
		for (Map.Entry<String, TemplateService> entry : creators.entrySet()) {
			if (Objects.equals(entry.getValue().type(), template)) {
				service = entry.getValue();
				break;
			}
		}
		ArgumentAssert.notNull(service, SystemErrorCode.PARAM_VALID_ERROR);
		return service;
	}

}
