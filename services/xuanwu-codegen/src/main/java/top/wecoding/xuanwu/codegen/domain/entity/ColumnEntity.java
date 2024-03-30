package top.wecoding.xuanwu.codegen.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;
import top.wecoding.xuanwu.codegen.enums.YesOrNo;
import top.wecoding.xuanwu.orm.domain.LogicDeleteEntity;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wecoding
 * @since 0.9
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "xw_table_column")
public class ColumnEntity extends LogicDeleteEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String columnName;

	private String columnComment;

	private String columnType;

	private String genType;

	private String genField;

	@JsonProperty("isPk")
	private String isPk;

	@JsonProperty("isIncrement")
	private String isIncrement;

	@JsonProperty("isRequired")
	private String isRequired;

	@JsonProperty("isInsert")
	private String isInsert;

	@JsonProperty("isEdit")
	private String isEdit;

	@JsonProperty("isList")
	private String isList;

	@JsonProperty("isQuery")
	private String isQuery;

	private String queryType;

	private String htmlType;

	private Integer sort;

	@JsonIgnore
	@JoinColumn(name = "table_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private TableEntity table;

	@JsonIgnore
	public static boolean isSuperColumn(String genField) {
		return StringUtils.equalsAnyIgnoreCase(genField,
				// BaseEntity
				"createdAt", "updatedAt", "deleted", "isDeleted",
				// TreeEntity
				"parentName", "parentId", "orderNum", "ancestors");
	}

	@JsonIgnore
	public static boolean isUsableColumn(String javaField) {
		// isSuperColumn()中的名单用于避免生成多余Domain属性，若某些属性在生成页面时需要用到不能忽略，则放在此处白名单
		return StringUtils.equalsAnyIgnoreCase(javaField, "parentId", "orderNum", "remark");
	}

	@JsonIgnore
	public boolean isSuperColumn() {
		return isSuperColumn(this.genField);
	}

	@JsonIgnore
	public boolean isUsableColumn() {
		return isUsableColumn(this.genField);
	}

	@JsonIgnore
	public boolean isPk() {
		return YesOrNo.YES.is(this.isPk);
	}

	@JsonIgnore
	public boolean isRequired() {
		return YesOrNo.YES.is(this.isRequired);
	}

	@JsonIgnore
	public boolean isIncrement() {
		return YesOrNo.YES.is(this.isIncrement);
	}

	@JsonIgnore
	public boolean isInsert() {
		return YesOrNo.YES.is(this.isInsert);
	}

	@JsonIgnore
	public boolean isList() {
		return YesOrNo.YES.is(this.isList);
	}

	@JsonIgnore
	public boolean isQuery() {
		return YesOrNo.YES.is(this.isQuery);
	}

	@JsonIgnore
	public boolean isEdit() {
		return YesOrNo.YES.is(this.isEdit);
	}

	@Override
	public String toString() {
		return "ColumnEntity{" + "id=" + id + ", columnName='" + columnName + '\'' + ", columnComment='" + columnComment
				+ '\'' + ", columnType='" + columnType + '\'' + ", genType='" + genType + '\'' + ", genField='"
				+ genField + '\'' + ", isPk='" + isPk + '\'' + ", isIncrement='" + isIncrement + '\'' + ", isRequired='"
				+ isRequired + '\'' + ", isInsert='" + isInsert + '\'' + ", isEdit='" + isEdit + '\'' + ", isList='"
				+ isList + '\'' + ", isQuery='" + isQuery + '\'' + ", queryType='" + queryType + '\'' + ", htmlType='"
				+ htmlType + '\'' + ", sort=" + sort + '}';
	}

}
