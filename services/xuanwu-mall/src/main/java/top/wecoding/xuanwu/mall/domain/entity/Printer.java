package top.wecoding.xuanwu.mall.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import top.wecoding.xuanwu.orm.domain.LogicDeleteEntity;

/**
 * mall_printer - 打印机
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-31 17:54:36
 */
@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "mall_printer")
public class Printer extends LogicDeleteEntity implements Serializable {

  @Serial private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  /** 是否启用 */
  @Column(name = "status")
  private Integer status;

  /** 打印机名称 */
  @Column(name = "name")
  private String name;

  @Column(name = "description")
  private String description;

  @Column(name = "type")
  private Integer type;

  @Column(name = "ip")
  private String ip;

  @Column(name = "port")
  private Integer port;

  @Column(name = "encoding")
  private String encoding;

  @Column(name = "print_mode")
  private Integer printMode;
}
