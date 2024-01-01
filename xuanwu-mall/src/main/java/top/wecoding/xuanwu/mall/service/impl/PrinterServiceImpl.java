package top.wecoding.xuanwu.mall.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import top.wecoding.xuanwu.core.base.PageResult;
import top.wecoding.xuanwu.core.exception.SystemErrorCode;
import top.wecoding.xuanwu.core.util.ArgumentAssert;
import top.wecoding.xuanwu.mall.constant.PrinterStatus;
import top.wecoding.xuanwu.mall.domain.entity.Printer;
import top.wecoding.xuanwu.mall.domain.request.PrinterServicePageRequest;
import top.wecoding.xuanwu.mall.domain.response.OrderDetail;
import top.wecoding.xuanwu.mall.repository.PrinterRepository;
import top.wecoding.xuanwu.mall.service.PrinterService;
import top.wecoding.xuanwu.mall.util.printer.SalesTicket;
import top.wecoding.xuanwu.mall.util.printer.SalesTicketPrinter;
import top.wecoding.xuanwu.orm.helper.QueryHelp;
import top.wecoding.xuanwu.orm.service.BaseServiceImpl;

import java.util.List;

/**
 * 打印机 - ServiceImpl
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-31 17:54:36
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PrinterServiceImpl extends BaseServiceImpl<Printer, Long> implements PrinterService {

	private final PrinterRepository printerRepository;

	@Override
	public List<Printer> getAvailablePrinterService() {
		return printerRepository.findByStatus(PrinterStatus.AVAILABLE.getStatus());
	}

	@Override
	public PageResult<Printer> listPrinterService(PrinterServicePageRequest query, Pageable pageable) {
		Page<Printer> pageResult = this.printerRepository.findAll(
				(root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, query, criteriaBuilder),
				pageable);
		return PageResult.of(pageResult.getContent(), pageResult.getTotalElements());
	}

	@Override
	public void printSalesTicket(OrderDetail orderDetail) {
		List<Printer> printerServices = getAvailablePrinterService();

		ArgumentAssert.notEmpty(printerServices, SystemErrorCode.PARAM_ERROR, "没有可用的打印机");

		for (Printer printerService : printerServices) {
			SalesTicketPrinter salesTicketPrinter = new SalesTicketPrinter(new SalesTicket(orderDetail));
			try {
				salesTicketPrinter.printer(printerService.getName());
			}
			catch (Exception e) {
				log.error("Failed to print sales ticket, order: {}", orderDetail.getOrder().getId(), e);
			}
		}
	}

	@Override
	protected JpaRepository<Printer, Long> getBaseRepository() {
		return this.printerRepository;
	}

}
