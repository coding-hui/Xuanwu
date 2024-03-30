package top.wecoding.xuanwu.codegen.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;

/**
 * @author wecoding
 * @since 0.8
 */
@Configuration
@ConditionalOnFrontendEnabled
public class ConsoleFrontendConfiguration implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**")
			.addResourceLocations("classpath:/frontend/")
			.resourceChain(true)
			.addResolver(new PathResourceResolver() {
				@Override
				protected Resource getResource(String resourcePath, Resource location) throws IOException {
					Resource resource = super.getResource(resourcePath, location);
					if (resource == null) {
						resource = super.getResource("index.html", location);
					}
					return resource;
				}
			});
	}

}
