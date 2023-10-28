package top.wecoding.codegen.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import top.wecoding.xuanwu.jpa.domain.LogicDeleteEntity;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wecoding
 * @since 0.9
 */
@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@Table(name = "xw_table_info")
public class TableInfo extends LogicDeleteEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String dsName;

	private String dbType;

	private String tableName;

	private String tableComment;

	private String className;

	private String author;

	private String email;

	private String packageName;

	private String version;

	private String generatorType;

	private String backendPath;

	private String frontendPath;

}
