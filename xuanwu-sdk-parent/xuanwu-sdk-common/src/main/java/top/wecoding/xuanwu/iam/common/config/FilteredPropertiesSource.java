package top.wecoding.xuanwu.iam.common.config;

import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author wecoding
 * @since 0.8
 */
public class FilteredPropertiesSource implements PropertiesSource {

	private final PropertiesSource propertiesSource;

	private final Filter filter;

	public FilteredPropertiesSource(PropertiesSource propertiesSource, Filter filter) {
		Assert.notNull(propertiesSource, "source cannot be null.");
		Assert.notNull(filter, "filter cannot be null.");
		this.propertiesSource = propertiesSource;
		this.filter = filter;
	}

	@Override
	public Map<String, String> getProperties() {

		Map<String, String> props = this.propertiesSource.getProperties();

		Map<String, String> retained = new LinkedHashMap<String, String>();

		if (!CollectionUtils.isEmpty(props)) {

			props.forEach((key, value) -> {
				String[] evaluated = filter.map(key, value);
				if (evaluated != null) {
					Assert.isTrue(2 == evaluated.length,
							"Filter returned string array must have a length of 2 (key/value pair)");
					retained.put(evaluated[0], evaluated[1]);
				}
			});
		}

		return retained;
	}

	public interface Filter {

		String[] map(String key, String value);

	}

}
