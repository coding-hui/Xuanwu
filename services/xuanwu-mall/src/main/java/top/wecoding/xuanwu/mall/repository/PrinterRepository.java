package top.wecoding.xuanwu.mall.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import top.wecoding.xuanwu.mall.domain.entity.Printer;
import top.wecoding.xuanwu.orm.repository.LogicDeleteRepository;

import java.util.List;

/**
 * 打印机 - Repository
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-31 17:54:36
 */
public interface PrinterRepository extends LogicDeleteRepository<Printer, Long>, JpaSpecificationExecutor<Printer> {

	Printer getByName(String name);

	boolean existsByName(String name);

	List<Printer> findByStatus(Integer status);

}
