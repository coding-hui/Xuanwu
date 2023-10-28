package top.wecoding.codegen.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import top.wecoding.codegen.config.CodeGenProperties;
import top.wecoding.codegen.domain.entity.ColumnEntity;
import top.wecoding.codegen.domain.entity.TableEntity;

import java.io.File;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static top.wecoding.codegen.constant.GenConstants.TYPE_BIGDECIMAL;
import static top.wecoding.codegen.constant.GenConstants.TYPE_DATE;
import static top.wecoding.xuanwu.core.constant.Constant.NORM_DATETIME_PATTERN;

/**
 * @author wecoding
 * @since 0.9
 */
@Slf4j
public abstract class AbstractTemplateService implements TemplateService {

	/** 项目空间路径 */
	private static final String PROJECT_PATH = "/main/java";

	protected VelocityContext prepareVelocityContext(TableEntity table) {
		List<ColumnEntity> columns = table.getColumns();
		VelocityContext velocityContext = new VelocityContext();
		velocityContext.put("tplCategory", table.getTplCategory());
		velocityContext.put("tableName", table.getTableName());
		velocityContext.put("tableComment", table.getTableComment());
		velocityContext.put("ClassName", table.getClassName());
		velocityContext.put("className", StringUtils.uncapitalize(table.getClassName()));
		velocityContext.put("packageName", table.getPackageName());
		velocityContext.put("author", table.getAuthor());
		velocityContext.put("version", StringUtils.replace(table.getVersion(), "-", "_"));
		velocityContext.put("datetime", DateFormatUtils.format(new Date(), NORM_DATETIME_PATTERN));
		velocityContext.put("table", table);
		velocityContext.put("columns", columns);
		velocityContext.put("pkColumn", getPkColumn(table));
		velocityContext.put("importList", getImportList(table));
		return velocityContext;
	}

	protected HashSet<String> getImportList(TableEntity genTable) {
		List<ColumnEntity> columns = genTable.getColumns();
		HashSet<String> importList = new HashSet<String>();
		for (ColumnEntity column : columns) {
			if (!column.isSuperColumn() && TYPE_DATE.equals(column.getGenType())) {
				importList.add("java.util.Date");
			}
			else if (!column.isSuperColumn() && TYPE_BIGDECIMAL.equals(column.getGenType())) {
				importList.add("java.math.BigDecimal");
			}
		}
		return importList;
	}

	protected ColumnEntity getPkColumn(TableEntity table) {
		List<ColumnEntity> columns = table.getColumns();
		Optional<ColumnEntity> pkColumn = columns.stream().filter(ColumnEntity::isPk).findFirst();
		return pkColumn.orElse(columns.get(0));
	}

	protected String getGenPath(TableEntity table, CodeGenProperties.Template template, final VelocityContext context) {
		String genPath;
		String packageName = table.getPackageName();
		String fileName = getFileName(context, template);
		if (isFrontendTemplate(template.getName())) {
			genPath = table.getFrontendPath();
		}
		else {
			genPath = table.getBackendPath() + PROJECT_PATH + "/" + StringUtils.replace(packageName, ".", "/");
		}
		if (StringUtils.equals(genPath, "/")) {
			return System.getProperty("user.dir") + File.separator + "src" + File.separator + fileName;
		}
		return genPath + File.separator + fileName;
	}

	protected String getFileName(VelocityContext context, CodeGenProperties.Template template) {
		try {
			StringWriter stringWriter = new StringWriter();
			Velocity.evaluate(context, stringWriter, "genTemplateFileName", template.getFileNameFormat());
			return stringWriter.toString();
		}
		catch (Exception e) {
			log.error("failed to render tpl file name [{}]", template.getName(), e);
		}
		return template.getName();
	}

	protected boolean isFrontendTemplate(String template) {
		return StringUtils.containsAny(template, "ts", "js", "vue", "tsx");
	}

}
