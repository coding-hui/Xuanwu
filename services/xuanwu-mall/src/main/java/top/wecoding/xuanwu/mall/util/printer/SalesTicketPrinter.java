package top.wecoding.xuanwu.mall.util.printer;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.PrinterName;

import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import lombok.extern.slf4j.Slf4j;

import top.wecoding.xuanwu.core.exception.SystemErrorCode;
import top.wecoding.xuanwu.core.util.ArgumentAssert;

/**
 * @author wecoding
 * @since 0.9
 */
@Slf4j
public class SalesTicketPrinter {

  private SalesTicket salesTicket;

  public SalesTicketPrinter(SalesTicket salesTicket) {
    this.salesTicket = salesTicket;
  }

  public void printer(String printerName) {
    // Book 类提供文档的表示形式，该文档的页面可以使用不同的页面格式和页面 painter
    // 要打印的文档
    Book book = new Book();
    // PageFormat类描述要打印的页面大小和方向
    // 初始化一个页面打印对象
    PageFormat pf = new PageFormat();
    // 设置页面打印方向，从上往下，从左往右
    pf.setOrientation(PageFormat.PORTRAIT);
    // 设置打印纸页面信息。通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。
    Paper paper = new Paper();
    MediaSize mediaSize = new MediaSize(58, 3276, MediaPrintableArea.MM);
    // paper.setSize(200,30000);// 纸张大小
    // paper.setImageableArea(0,0,200,30000);// A4(595 X
    // 842)设置打印区域，其实0，0应该是72，72，因为A4纸的默认X,Y边距是72
    paper.setSize((double) mediaSize.getX(25400) * 72.0D, (double) mediaSize.getY(25400) * 72.0D);
    paper.setImageableArea(0.0D, 0.0D, paper.getWidth(), paper.getHeight());
    pf.setPaper(paper);
    book.append(salesTicket, pf);
    HashAttributeSet hs = new HashAttributeSet();
    hs.add(new PrinterName(printerName, null));
    PrintService[] pss = PrintServiceLookup.lookupPrintServices(null, hs);
    ArgumentAssert.notEmpty(pss, SystemErrorCode.FAILURE, "无可用打印机");
    // 获取打印服务对象
    PrinterJob job = PrinterJob.getPrinterJob();
    // 设置打印类
    job.setPageable(book);
    try {
      // 添加指定的打印机
      job.setPrintService(pss[0]);
      // 打印执行
      job.print();
    } catch (PrinterException e) {
      log.error("Failed to print sales ticket: {}", printerName, e);
      ArgumentAssert.notEmpty(pss, SystemErrorCode.FAILURE, "打印失败，请检查打印机配置");
    }
  }
}
