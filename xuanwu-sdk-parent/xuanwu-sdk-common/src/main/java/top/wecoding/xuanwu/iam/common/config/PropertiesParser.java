package top.wecoding.xuanwu.iam.common.config;

import top.wecoding.xuanwu.iam.common.io.Resource;

import java.io.IOException;
import java.util.Map;
import java.util.Scanner;

/**
 * @author wecoding
 * @since 0.8
 */
public interface PropertiesParser {

	Map<String, String> parse(String source);

	Map<String, String> parse(Resource resource) throws IOException;

	Map<String, String> parse(Scanner source);

}
