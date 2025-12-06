package top.wecoding.xuanwu.iam.common.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author wecoding
 * @since 0.8
 */
public class FileResource extends AbstractResource {

    private static final String SCHEME = "file";

    public FileResource(String location) {
        super(location);
    }

    @Override
    protected String getScheme() {
        return SCHEME;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return Files.newInputStream(Paths.get(getLocation()));
    }

}
