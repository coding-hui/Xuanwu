package top.wecoding.xuanwu.judge.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import top.wecoding.xuanwu.orm.domain.LogicDeleteEntity;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author wecoding
 * @since 0.10
 */
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "oj_problem")
public class Problem extends LogicDeleteEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "code", unique = true, length = 64)
	private String code;

	@Version
	@Column(name = "version")
	private Integer version;

	@Column(name = "problem_title")
	private String problemTitle;

	@Column(name = "source")
	private String source;

	@Column(name = "submit_num")
	private Integer submitNum;

	@Column(name = "accept_num")
	private Integer acceptNum;

	@Column(name = "memory_limit")
	private Integer memoryLimit;

	@Column(name = "time_limit")
	private Integer timeLimit;

	@Column(name = "checkpoint_num")
	private Integer checkpointNum;

	@Column(name = "checkpoints")
	private byte[] checkpoints;

	@Column(name = "checkpoint_cases")
	private byte[] checkpointCases;

	@Column(name = "judge_templates")
	private String judgeTemplates;

}
