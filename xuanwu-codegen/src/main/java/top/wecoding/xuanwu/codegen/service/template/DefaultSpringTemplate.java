package top.wecoding.xuanwu.codegen.service.template;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import top.wecoding.xuanwu.codegen.config.CodeGenConfig;
import top.wecoding.xuanwu.codegen.domain.entity.ColumnEntity;
import top.wecoding.xuanwu.codegen.domain.entity.TableEntity;
import top.wecoding.xuanwu.codegen.enums.GeneratorType;
import top.wecoding.xuanwu.codegen.service.AbstractTemplateService;
import top.wecoding.xuanwu.codegen.util.Strings;
import top.wecoding.xuanwu.codegen.util.VelocityInitializer;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static top.wecoding.xuanwu.codegen.constant.GenConstants.COLUMNNAME_NOT_EDIT;
import static top.wecoding.xuanwu.codegen.constant.GenConstants.COLUMNNAME_NOT_LIST;
import static top.wecoding.xuanwu.codegen.constant.GenConstants.COLUMNNAME_NOT_QUERY;
import static top.wecoding.xuanwu.codegen.constant.GenConstants.COLUMNTYPE_NUMBER;
import static top.wecoding.xuanwu.codegen.constant.GenConstants.COLUMNTYPE_STR;
import static top.wecoding.xuanwu.codegen.constant.GenConstants.COLUMNTYPE_TEXT;
import static top.wecoding.xuanwu.codegen.constant.GenConstants.COLUMNTYPE_TIME;
import static top.wecoding.xuanwu.codegen.constant.GenConstants.HTML_DATETIME;
import static top.wecoding.xuanwu.codegen.constant.GenConstants.HTML_EDITOR;
import static top.wecoding.xuanwu.codegen.constant.GenConstants.HTML_FILE_UPLOAD;
import static top.wecoding.xuanwu.codegen.constant.GenConstants.HTML_IMAGE_UPLOAD;
import static top.wecoding.xuanwu.codegen.constant.GenConstants.HTML_INPUT;
import static top.wecoding.xuanwu.codegen.constant.GenConstants.HTML_RADIO;
import static top.wecoding.xuanwu.codegen.constant.GenConstants.HTML_SELECT;
import static top.wecoding.xuanwu.codegen.constant.GenConstants.HTML_TEXTAREA;
import static top.wecoding.xuanwu.codegen.constant.GenConstants.QUERY_EQ;
import static top.wecoding.xuanwu.codegen.constant.GenConstants.QUERY_LIKE;
import static top.wecoding.xuanwu.codegen.constant.GenConstants.REQUIRE;
import static top.wecoding.xuanwu.codegen.constant.GenConstants.TYPE_BIGDECIMAL;
import static top.wecoding.xuanwu.codegen.constant.GenConstants.TYPE_DATE;
import static top.wecoding.xuanwu.codegen.constant.GenConstants.TYPE_INTEGER;
import static top.wecoding.xuanwu.codegen.constant.GenConstants.TYPE_LONG;
import static top.wecoding.xuanwu.codegen.constant.GenConstants.TYPE_STRING;
import static top.wecoding.xuanwu.codegen.util.Strings.getBusinessName;
import static top.wecoding.xuanwu.codegen.util.Strings.getModuleName;
import static top.wecoding.xuanwu.codegen.util.Strings.toClassName;
import static top.wecoding.xuanwu.core.constant.StrPool.UTF8;

/**
 * @author wecoding
 * @since 0.9
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultSpringTemplate extends AbstractTemplateService {

	public static final String TYPE = "DEFAULT_SPRING";

	private final CodeGenConfig genConf;

	@Override
	public String type() {
		return TYPE;
	}

	@Override
	public void initTableConfig(TableEntity table, List<ColumnEntity> columns) {
		// init table config
		table.setTplCategory(TYPE);
		table.setAuthor(genConf.getAuthor());
		table.setGenType(GeneratorType.ZIP.name());
		table.setPackageName(genConf.getPackageName());
		table.setModuleName(getModuleName(table.getPackageName()));
		table.setBusinessName(getBusinessName(table.getTableName()));
		table.setFunctionName(table.getTableComment());
		table.setClassName(toClassName(table.getTableName(), genConf.getTablePrefix(), genConf.isAutoRemovePre()));
		table.setVersion(DEFAULT_VERSION);

		// init table column config
		for (ColumnEntity column : columns) {
			String dataType = Strings.getDbType(column.getColumnType());
			String columnName = column.getColumnName();
			column.setTable(table);
			column.setGenField(Strings.toCamelCase(columnName));
			// 设置默认类型
			column.setGenType(TYPE_STRING);
			column.setQueryType(QUERY_EQ);

			if (ArrayUtils.contains(COLUMNTYPE_STR, dataType) || ArrayUtils.contains(COLUMNTYPE_TEXT, dataType)) {
				// 字符串长度超过500设置为文本域
				Integer columnLength = Strings.getColumnLength(column.getColumnType());
				String htmlType = columnLength >= 500 || ArrayUtils.contains(COLUMNTYPE_TEXT, dataType) ? HTML_TEXTAREA
						: HTML_INPUT;
				column.setHtmlType(htmlType);
			}
			else if (ArrayUtils.contains(COLUMNTYPE_TIME, dataType)) {
				column.setGenType(TYPE_DATE);
				column.setHtmlType(HTML_DATETIME);
			}
			else if (ArrayUtils.contains(COLUMNTYPE_NUMBER, dataType)) {
				column.setHtmlType(HTML_INPUT);

				// 如果是浮点型 统一用BigDecimal
				String[] str = StringUtils.split(StringUtils.substringBetween(column.getColumnType(), "(", ")"), ",");
				if (str != null && str.length == 2 && Integer.parseInt(str[1]) > 0) {
					column.setGenType(TYPE_BIGDECIMAL);
				}
				// 如果是整形
				else if (str != null && str.length == 1 && Integer.parseInt(str[0]) <= 10) {
					column.setGenType(TYPE_INTEGER);
				}
				// 长整形
				else {
					column.setGenType(TYPE_LONG);
				}
			}

			// 插入字段（默认所有字段都需要插入）
			column.setIsInsert(REQUIRE);

			// 编辑字段
			if (!ArrayUtils.contains(COLUMNNAME_NOT_EDIT, columnName) && !column.isPk()) {
				column.setIsEdit(REQUIRE);
			}
			// 列表字段
			if (!ArrayUtils.contains(COLUMNNAME_NOT_LIST, columnName) && !column.isPk()) {
				column.setIsList(REQUIRE);
			}
			// 查询字段
			if (!ArrayUtils.contains(COLUMNNAME_NOT_QUERY, columnName) && !column.isPk()) {
				column.setIsQuery(REQUIRE);
			}

			// 查询字段类型
			if (StringUtils.endsWithIgnoreCase(columnName, "name")) {
				column.setQueryType(QUERY_LIKE);
			}
			// 状态字段设置单选框
			if (StringUtils.endsWithIgnoreCase(columnName, "status")) {
				column.setHtmlType(HTML_RADIO);
			}
			// 类型&性别字段设置下拉框
			else if (StringUtils.endsWithIgnoreCase(columnName, "type")
					|| StringUtils.endsWithIgnoreCase(columnName, "sex")) {
				column.setHtmlType(HTML_SELECT);
			}
			// 图片字段设置图片上传控件
			else if (StringUtils.endsWithIgnoreCase(columnName, "image")) {
				column.setHtmlType(HTML_IMAGE_UPLOAD);
			}
			// 文件字段设置文件上传控件
			else if (StringUtils.endsWithIgnoreCase(columnName, "file")) {
				column.setHtmlType(HTML_FILE_UPLOAD);
			}
			// 内容字段设置富文本控件
			else if (StringUtils.endsWithIgnoreCase(columnName, "content")) {
				column.setHtmlType(HTML_EDITOR);
			}
		}
		// update columns
		table.setColumns(columns);
	}

	@Override
	public Map<String, String> render(TableEntity tableEntity) {
		VelocityInitializer.initialize();
		var templates = genConf.getTemplates().get(TYPE);
		if (CollectionUtils.isEmpty(templates)) {
			log.warn("template [{}] cannot found any templates", TYPE);
			return new HashMap<>();
		}
		Map<String, String> result = new LinkedHashMap<>();
		VelocityContext context = prepareVelocityContext(tableEntity);
		for (var template : templates) {
			StringWriter sw = new StringWriter();
			Template tpl = Velocity.getTemplate(template.getName(), UTF8);
			tpl.merge(context, sw);
			result.put(template.getName(), sw.toString());
		}
		return result;
	}

	@Override
	public void renderToFile(TableEntity table) {
		VelocityInitializer.initialize();
		var templates = genConf.getTemplates().get(TYPE);
		if (CollectionUtils.isEmpty(templates)) {
			log.warn("template [{}] cannot found any templates", TYPE);
			return;
		}
		VelocityContext context = prepareVelocityContext(table);
		for (var template : templates) {
			try {
				StringWriter sw = new StringWriter();
				Template tpl = Velocity.getTemplate(template.getName(), UTF8);
				tpl.merge(context, sw);
				File outFile = new File(getGenPath(table, template, context));
				if (!outFile.getParentFile().exists()) {
					outFile.getParentFile().mkdirs();
				}
				FileCopyUtils.copy(sw.toString().getBytes(StandardCharsets.UTF_8), outFile);
			}
			catch (IOException e) {
				log.error("failed to render template [{}] to table [{}]", template, table.getTableName(), e);
			}
		}
	}

	@Override
	public void renderToZipStream(TableEntity table, ZipOutputStream zip) {
		VelocityInitializer.initialize();
		var templates = genConf.getTemplates().get(TYPE);
		if (CollectionUtils.isEmpty(templates)) {
			log.warn("template [{}] cannot found any templates", TYPE);
			return;
		}
		VelocityContext context = prepareVelocityContext(table);
		for (var template : templates) {
			try {
				StringWriter sw = new StringWriter();
				Template tpl = Velocity.getTemplate(template.getName(), UTF8);
				tpl.merge(context, sw);
				zip.putNextEntry(new ZipEntry(getFileName(context, template)));
				IOUtils.write(sw.toString(), zip, UTF8);
				IOUtils.closeQuietly(sw);
				zip.flush();
				zip.closeEntry();
			}
			catch (IOException e) {
				log.error("failed to render template [{}] to table [{}]", template, table.getTableName(), e);
			}
		}
	}

}
