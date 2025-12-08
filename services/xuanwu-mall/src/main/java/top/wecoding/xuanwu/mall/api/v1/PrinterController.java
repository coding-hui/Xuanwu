package top.wecoding.xuanwu.mall.api.v1;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import top.wecoding.xuanwu.core.annotation.Version;
import top.wecoding.xuanwu.core.base.R;
import top.wecoding.xuanwu.core.exception.DateNotFoundException;
import top.wecoding.xuanwu.mall.domain.entity.Printer;
import top.wecoding.xuanwu.mall.domain.request.PrinterServicePageRequest;
import top.wecoding.xuanwu.mall.domain.response.OrderDetail;
import top.wecoding.xuanwu.mall.service.OrderService;
import top.wecoding.xuanwu.mall.service.PrinterService;

/**
 * 打印机 - API Controller
 *
 * @author Xuanwu
 * @since v1
 * @date 2023-12-31 17:54:36
 */
@Version("v1")
@RequiredArgsConstructor
@RestController("printerController.v1")
@RequestMapping("/printer")
public class PrinterController {

  private final PrinterService printerService;

  private final OrderService orderService;

  @GetMapping("/{id}")
  public R<Printer> getInfo(@PathVariable("id") Long id) {
    return R.ok(printerService.getById(id).orElseThrow(DateNotFoundException::new));
  }

  @GetMapping("/service")
  public R<?> paging(@PageableDefault Pageable pageReq, PrinterServicePageRequest queryParams) {
    return R.ok(printerService.listPrinterService(queryParams, pageReq));
  }

  @PostMapping("")
  public R<Printer> create(@RequestBody @Validated Printer printer) {
    return R.ok(printerService.create(printer));
  }

  @PutMapping("/{id}")
  public R<Printer> update(@PathVariable("id") Long id, @RequestBody @Validated Printer printer) {
    return R.ok(printerService.updateById(id, printer));
  }

  @DeleteMapping("/{id}")
  public R<?> delete(@PathVariable("id") Long id) {
    printerService.deleteById(id);
    return R.ok();
  }

  @GetMapping("/print_test_page/{id}")
  public R<?> printTestPage(@PathVariable("id") Long id) {
    return R.ok(printerService.printTestPage(id));
  }

  @GetMapping("/submit_print_job/{orderId}")
  public R<?> submitPrintJob(
      @PathVariable("orderId") Long orderId, @RequestParam("type") Integer type) {
    OrderDetail detail = orderService.detail(orderId);
    printerService.printSalesTicket(detail, type);
    return R.ok();
  }
}
