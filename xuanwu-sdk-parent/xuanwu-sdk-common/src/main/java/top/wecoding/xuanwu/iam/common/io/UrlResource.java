package top.wecoding.xuanwu.iam.common.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * @author wecoding
 * @since 0.8
 */
public class UrlResource extends AbstractResource {

	private static final String SCHEME = "url";

	public static final String SCHEME_PREFIX = SCHEME + ":";

	public UrlResource(String location) {
		super(location);
	}

	@Override
	protected String getScheme() {
		return SCHEME;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		URL url = new URL(getLocation());
		return url.openStream();
	}

}
