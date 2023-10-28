package top.wecoding.codegen.service;

import top.wecoding.codegen.domain.entity.ColumnEntity;
import top.wecoding.codegen.domain.entity.TableEntity;

import java.util.List;
import java.util.Map;
import java.util.zip.ZipOutputStream;

/**
 * @author wecoding
 * @since 0.9
 */
public interface TemplateService {

	String type();

	void initTableConfig(TableEntity table, List<ColumnEntity> columns);

	Map<String, String> render(TableEntity table);

	void renderToFile(TableEntity table);

	void renderToZipStream(TableEntity table, ZipOutputStream zip);

}
