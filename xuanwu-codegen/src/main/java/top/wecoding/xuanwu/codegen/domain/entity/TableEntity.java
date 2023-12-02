package top.wecoding.xuanwu.codegen.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.wecoding.xuanwu.orm.domain.LogicDeleteEntity;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author wecoding
 * @since 0.9
 */
@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "xw_table_info")
public class TableEntity extends LogicDeleteEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String tplCategory;

	private String dsName;

	private String dbType;

	private String tableName;

	private String tableComment;

	private String className;

	private String author;

	private String packageName;

	@Pattern(message = "版本名称必须由字母、数字和 - 组成，长度为 1~64", regexp = "^(?!-)[a-zA-Z0-9-]{1,63}(?<!-)$")
	private String version;

	private String genType;

	private String backendPath;

	private String frontendPath;

	private String dbEngine;

	private String tableCollation;

	/** 功能名称 用作类描述，例如 用户 */
	private String functionName;

	/** 生成业务名 可理解为功能英文名，例如 user */
	private String businessName;

	/** 生成模块名 可理解为子系统名，例如 system */
	private String moduleName;

	@OneToMany(mappedBy = "table", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private List<ColumnEntity> columns;

	private String remark;

}
