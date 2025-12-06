package top.wecoding.xuanwu.mall.service;

import org.springframework.data.domain.Pageable;
import top.wecoding.xuanwu.core.base.PageResult;
import top.wecoding.xuanwu.mall.domain.entity.Printer;
import top.wecoding.xuanwu.mall.domain.request.PrinterServicePageRequest;
import top.wecoding.xuanwu.mall.domain.response.OrderDetail;
import top.wecoding.xuanwu.orm.service.BaseService;

import java.util.List;

/**
 * 打印机 - Service
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-31 17:54:36
 */
public interface PrinterService extends BaseService<Printer, Long> {

    List<Printer> getAvailablePrinterService();

    PageResult<Printer> listPrinterService(PrinterServicePageRequest query, Pageable pageable);

    void printSalesTicket(OrderDetail orderDetail, Integer type);

    boolean printTestPage(Long id);

}
