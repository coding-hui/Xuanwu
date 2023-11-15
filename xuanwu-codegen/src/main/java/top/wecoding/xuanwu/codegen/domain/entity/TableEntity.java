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
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

	private String generatorType;

	private String backendPath;

	private String frontendPath;

	private String dbEngine;

	private String tableCollation;

	@OneToMany(mappedBy = "table", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
	private List<ColumnEntity> columns;

}
