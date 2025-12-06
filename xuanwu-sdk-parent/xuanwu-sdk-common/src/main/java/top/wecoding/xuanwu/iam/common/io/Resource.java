package top.wecoding.xuanwu.iam.common.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author wecoding
 * @since 0.8
 */
public interface Resource {

    InputStream getInputStream() throws IOException;

}
