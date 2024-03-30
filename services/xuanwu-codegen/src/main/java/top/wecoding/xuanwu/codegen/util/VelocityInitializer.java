package top.wecoding.xuanwu.codegen.util;

import org.apache.velocity.app.Velocity;
import top.wecoding.xuanwu.core.helper.ExceptionHelper;

import java.util.Properties;

import static org.apache.velocity.runtime.RuntimeConstants.INPUT_ENCODING;
import static top.wecoding.xuanwu.core.constant.StrPool.UTF8;

/**
 * @author wecoding
 * @since 0.9
 */
public class VelocityInitializer {

	public static void initialize() {
		Properties p = new Properties();
		try {
			// 加载classpath目录下的vm文件
			p.setProperty("resource.loader.file.class",
					"org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			// 定义字符集
			p.setProperty(INPUT_ENCODING, UTF8);
			// 初始化Velocity引擎，指定配置Properties
			Velocity.init(p);
		}
		catch (Exception e) {
			throw ExceptionHelper.unchecked(e);
		}
	}

}
