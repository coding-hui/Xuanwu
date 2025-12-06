package top.wecoding.xuanwu.mall.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import top.wecoding.xuanwu.core.base.PageResult;
import top.wecoding.xuanwu.core.exception.SystemErrorCode;
import top.wecoding.xuanwu.core.util.ArgumentAssert;
import top.wecoding.xuanwu.core.util.JsonUtil;
import top.wecoding.xuanwu.mall.domain.entity.Printer;
import top.wecoding.xuanwu.mall.domain.request.PrinterServicePageRequest;
import top.wecoding.xuanwu.mall.domain.response.OrderDetail;
import top.wecoding.xuanwu.mall.repository.PrinterRepository;
import top.wecoding.xuanwu.mall.service.PrinterService;
import top.wecoding.xuanwu.mall.util.printer.SalesTicket;
import top.wecoding.xuanwu.mall.util.printer.SalesTicketPrinter;
import top.wecoding.xuanwu.orm.helper.QueryHelp;
import top.wecoding.xuanwu.orm.service.BaseServiceImpl;
import top.wecoding.xuanwu.printer.config.PrinterConfig;
import top.wecoding.xuanwu.printer.escpos.EscPos;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static top.wecoding.xuanwu.core.exception.SystemErrorCode.FAILURE;
import static top.wecoding.xuanwu.core.exception.SystemErrorCode.PARAM_ERROR;
import static top.wecoding.xuanwu.mall.constant.PrinterStatus.AVAILABLE;
import static top.wecoding.xuanwu.mall.constant.PrinterType.POS;
import static top.wecoding.xuanwu.mall.constant.PrinterType.USB;

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

    private final PrinterConfig printerConfig;

    @Override
    public List<Printer> getAvailablePrinterService() {
        return printerRepository.findByStatus(AVAILABLE.getStatus());
    }

    @Override
    public PageResult<Printer> listPrinterService(PrinterServicePageRequest query, Pageable pageable) {
        Page<Printer> pageResult = this.printerRepository.findAll(
                (root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, query, criteriaBuilder),
                pageable);
        return PageResult.of(pageResult.getContent(), pageResult.getTotalElements());
    }

    @Override
    @SneakyThrows
    public void printSalesTicket(OrderDetail orderDetail, Integer printType) {
        List<Printer> printerServices = getAvailablePrinterService();

        ArgumentAssert.notEmpty(printerServices, PARAM_ERROR, "没有可用的打印机");

        for (Printer printerService : printerServices) {
            Integer type = printerService.getType();
            try {
                if (POS.is(type) && printerService.getPrintMode().equals(printType)) {
                    printEscPosPage(printerService, orderDetail);
                }
                else if (USB.is(type) && printerService.getPrintMode().equals(printType)) {
                    SalesTicket salesTicket = new SalesTicket(orderDetail);
                    new SalesTicketPrinter(salesTicket).printer(printerService.getName());
                }
            }
            catch (Exception e) {
                log.error("Failed to print sales ticket, order: {}, printer: {}", orderDetail.getOrder().getId(),
                        printerService.getName(), e);
            }
        }
    }

    @Override
    @SneakyThrows
    public boolean printTestPage(Long id) {
        Optional<Printer> printerOptional = getById(id);
        if (printerOptional.isEmpty()) {
            ArgumentAssert.error(SystemErrorCode.DATA_NOT_EXIST);
        }

        String orderDetailJsonStr = printerConfig.getDemoOrder().getContentAsString(StandardCharsets.UTF_8);

        OrderDetail orderDetail = JsonUtil.readValue(orderDetailJsonStr, OrderDetail.class);

        ArgumentAssert.notNull(orderDetail, FAILURE, "加载测试订单失败");

        Printer printer = printerOptional.get();

        ArgumentAssert.isTrue(AVAILABLE.is(printer.getStatus()), PARAM_ERROR, "打印机未启用");

        Integer type = printer.getType();

        if (POS.is(type)) {
            return printEscPosPage(printer, orderDetail);
        }
        else if (USB.is(type)) {
            SalesTicket salesTicket = new SalesTicket(orderDetail);
            new SalesTicketPrinter(salesTicket).printer(printer.getName());
            return true;
        }
        log.warn("Unsupported printer type: {}", type);
        return false;
    }

    @Override
    protected JpaRepository<Printer, Long> getBaseRepository() {
        return this.printerRepository;
    }

    private boolean printEscPosPage(Printer printer, OrderDetail orderDetail) {
        try {
            EscPos.getInstance(printer.getIp(), printer.getPort());
            String kitchenTpl = printerConfig.getKitchenTemplate().getContentAsString(StandardCharsets.UTF_8);
            Map<String, Object> params = new HashMap<>();
            params.put("keys", orderDetail.getOrder());
            params.put("goods", orderDetail.getOrderItems());
            EscPos.print(kitchenTpl, JsonUtil.toJsonStr(params));
            return true;
        }
        catch (Exception e) {
            log.error("Failed to print EscPos page: ", e);
            ArgumentAssert.error(FAILURE, e.getLocalizedMessage());
        }
        return false;
    }

}
