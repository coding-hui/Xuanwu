package top.wecoding.xuanwu.iam.common.io;

import top.wecoding.xuanwu.core.util.Classes;

import java.io.InputStream;

/**
 * @author wecoding
 * @since 0.8
 */
public class ClasspathResource extends AbstractResource {

    private static final String SCHEME = "classpath";

    public static final String SCHEME_PREFIX = SCHEME + ":";

    public ClasspathResource(String location) {
        super(location);
    }

    @Override
    protected String getScheme() {
        return SCHEME;
    }

    @Override
    public InputStream getInputStream() {
        return Classes.getResourceAsStream(getLocation());
    }

    @Override
    public String toString() {
        return SCHEME_PREFIX + getLocation();
    }

}
