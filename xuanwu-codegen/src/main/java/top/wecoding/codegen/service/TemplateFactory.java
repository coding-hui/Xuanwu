package top.wecoding.codegen.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.wecoding.codegen.service.template.DefaultSpringTemplate;
import top.wecoding.xuanwu.core.exception.IllegalParameterException;
import top.wecoding.xuanwu.core.util.ArgumentAssert;

import java.util.Map;
import java.util.Objects;

import static top.wecoding.xuanwu.core.exception.SystemErrorCode.PARAM_VALID_ERROR;

/**
 * @author wecoding
 * @since 0.9
 */
@Service
public class TemplateFactory {

	private static final String DEFAULT_TEMPLATE = DefaultSpringTemplate.TYPE;

	private final Map<String, TemplateService> creators;

	@Autowired
	public TemplateFactory(Map<String, TemplateService> creators) {
		this.creators = creators;
	}

	public TemplateService create(String template) {
		if (StringUtils.isBlank(template)) {
			return createDefault();
		}
		TemplateService service = creators.values()
			.stream()
			.filter(entry -> Objects.equals(entry.type(), template))
			.findFirst()
			.orElse(createDefault());
		ArgumentAssert.notNull(service, PARAM_VALID_ERROR);
		return service;
	}

	public TemplateService createDefault() {
		return creators.values()
			.stream()
			.filter(entry -> Objects.equals(entry.type(), DEFAULT_TEMPLATE))
			.findFirst()
			.orElseThrow(() -> new IllegalParameterException(PARAM_VALID_ERROR));
	}

}
