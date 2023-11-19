package top.wecoding.xuanwu.codegen.service;

import java.util.Map;

/**
 * @author wecoding
 * @since 0.9
 */
public interface GeneratorService {

	void generator(String tableIdOrName);

	byte[] download(String tableIdOrName);

	Map<String, String> preview(String tableIdOrName);

}
