package top.wecoding.xuanwu.iam.common.io;

import lombok.Getter;
import org.springframework.util.Assert;

/**
 * @author wecoding
 * @since 0.8
 */
@Getter
public abstract class AbstractResource implements Resource {

    private final String location;

    public AbstractResource(String location) {
        Assert.hasText(location, "Location argument cannot be null or empty.");
        this.location = canonicalize(location);
    }

    private static String stripPrefix(String resourcePath) {
        return resourcePath.substring(resourcePath.indexOf(":") + 1);
    }

    protected String canonicalize(String input) {
        if (hasResourcePrefix(input)) {
            input = stripPrefix(input);
        }
        return input;
    }

    protected boolean hasResourcePrefix(String resourcePath) {
        return resourcePath != null && resourcePath.startsWith(getScheme() + ":");
    }

    protected abstract String getScheme();

    @Override
    public String toString() {
        return getScheme() + location;
    }

}
