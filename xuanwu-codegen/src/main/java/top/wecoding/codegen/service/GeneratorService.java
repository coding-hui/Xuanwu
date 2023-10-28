package top.wecoding.codegen.service;

import java.util.Map;

/**
 * @author wecoding
 * @since 0.9
 */
public interface GeneratorService {

	void generator(Long tableId);

	byte[] download(Long tableId);

	Map<String, String> preview(Long tableId);

}
