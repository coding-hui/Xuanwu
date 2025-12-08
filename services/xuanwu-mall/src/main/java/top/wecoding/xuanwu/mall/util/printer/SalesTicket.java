package top.wecoding.xuanwu.mall.util.printer;

import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.util.CollectionUtils;
import top.wecoding.xuanwu.mall.domain.entity.Order;
import top.wecoding.xuanwu.mall.domain.entity.OrderItem;
import top.wecoding.xuanwu.mall.domain.response.OrderDetail;
import top.wecoding.xuanwu.mall.util.PriceUtil;

/**
 * @author wecoding
 * @since 0.9
 */
public class SalesTicket implements Printable {

  private final DateTimeFormatter timeFormatter =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

  private Order order;

  private List<OrderItem> orderItems;

  private String orgName = "三味鱼火锅";

  public SalesTicket(OrderDetail orderDetail) {
    super();
    this.order = orderDetail.getOrder();
    this.orderItems = orderDetail.getOrderItems();
  }

  /**
   * 打印方法
   *
   * @param graphics - 用来绘制页面的上下文，即打印的图形
   * @param pageFormat - 将绘制页面的大小和方向，即设置打印格式，如页面大小一点为计量单位（以1/72 英寸为单位，1英寸为25.4毫米。A4纸大致为595 × 842点）
   *     小票纸宽度一般为58mm，大概为165点
   * @param pageIndex - 要绘制的页面从 0 开始的索引 ，即页号
   */
  @Override
  public int print(Graphics graphics, PageFormat pageFormat, int pageIndex)
      throws PrinterException {
    // @formatter:off
    // 此 Graphics2D 类扩展 Graphics 类，以提供对几何形状、坐标转换、颜色管理和文本布局更为复杂的控制。
    // 它是用于在 Java(tm) 平台上呈现二维形状、文本和图像的基础类。
    Graphics2D g2 = (Graphics2D) graphics;
    // 设置打印颜色为黑色
    g2.setColor(Color.black);
    // 虚线设置
    g2.setStroke(
        new BasicStroke(
            0.5f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 4.0f, new float[] {4.0f}, 0.0f));

    // Font.PLAIN： 普通样式常量 Font.ITALIC 斜体样式常量 Font.BOLD 粗体样式常量。
    // 根据指定名称、样式和磅值大小，创建一个新 Font。
    Font titleFont = new Font("宋体", Font.BOLD, 12); // 标题字体
    Font contentFont = new Font("宋体", Font.PLAIN, 8); // 正文内容字体
    Font boldContentFont = new Font("宋体", Font.BOLD, 12); // 加粗字体

    // 打印起点坐标
    // 返回与此 PageFormat相关的 Paper对象的可成像区域左上方点的 x坐标。
    double x = pageFormat.getImageableX();
    // 返回与此 PageFormat相关的 Paper对象的可成像区域左上方点的 y坐标。
    double y = pageFormat.getImageableY();
    // 页面宽度
    double maxWidth = pageFormat.getImageableWidth();

    // 打印标题
    // 设置标题打印字体
    g2.setFont(titleFont);
    // 获取字体的高度
    float heigth = titleFont.getSize2D();
    // 设置小票的标题
    FontMetrics fm = g2.getFontMetrics(titleFont);
    int textWidth = fm.stringWidth(orgName);
    // 获取标题居中宽度
    int widthX = (210 - textWidth) / 2;
    g2.drawString(orgName, (float) x + widthX, (float) y + heigth);
    heigth += heigth;
    textWidth = fm.stringWidth("结算单");
    widthX = (210 - textWidth) / 2;
    g2.drawString("结算单", (float) x + widthX, (float) y + heigth);
    heigth += heigth;
    g2.drawString("桌号:" + order.getTableCode(), (float) x + 5, (float) y + heigth);
    textWidth = fm.stringWidth("人数:" + order.getPeopleCount());
    g2.drawString("人数:" + order.getPeopleCount(), 200 - textWidth, (float) y + heigth);
    // 0.25不能改
    heigth += heigth * 0.25;
    // 下一行开始打印的高度
    float line = heigth;
    g2.drawLine((int) x, (int) (y + line), (int) x + 227, (int) (y + line));
    line += 15;

    // 设置正文字体
    g2.setFont(contentFont);
    // 字体高度
    heigth = contentFont.getSize2D();
    // 设置单号
    g2.drawString("单号:" + order.getOrderSn(), (float) x + 5, (float) y + line);
    line += heigth;
    // 设置收银员
    g2.drawString("收银员:" + "管理员", (float) x + 5, (float) y + line);
    line += heigth;
    // 设置开台时间
    g2.drawString(
        "下单时间:" + order.getCreatedAt().format(timeFormatter), (float) x + 5, (float) y + line);
    line += heigth;

    g2.drawLine((int) x, (int) (y + line), (int) x + 227, (int) (y + line));
    line += heigth;

    // 设置标题
    g2.drawString("菜品", (float) x + 5, (float) y + line);
    g2.drawString("数量", (float) maxWidth - 100, (float) y + line);
    g2.drawString("单价", (float) maxWidth - 60, (float) y + line);
    g2.drawString("小计", (float) maxWidth - 30, (float) y + line);
    line += heigth;

    g2.drawLine((int) x, (int) (y + line), (int) x + 227, (int) (y + line));
    line += heigth;
    // 设置商品清单
    if (!CollectionUtils.isEmpty(orderItems)) {
      for (OrderItem orderItem : orderItems) {
        g2.drawString(orderItem.getFoodName(), (float) x + 5, (float) y + line);
        g2.drawString(
            orderItem.getFoodQuantity().toString(), (float) maxWidth - 100, (float) y + line);
        g2.drawString(orderItem.getFoodPrice().toString(), (float) maxWidth - 60, (float) y + line);
        g2.drawString(
            orderItem
                .getFoodPrice()
                .multiply(BigDecimal.valueOf(orderItem.getFoodQuantity()))
                .toString(),
            (float) maxWidth - 30,
            (float) y + line);
        line += heigth + 3;
      }
    }
    g2.drawLine((int) x, (int) (y + line), (int) x + 227, (int) (y + line));
    heigth = boldContentFont.getSize2D();
    line += heigth;
    g2.setFont(boldContentFont);
    g2.drawString("应付金额 ", (float) x + 5, (float) y + line);
    BigDecimal totalAmount = PriceUtil.calcOrderItemTotalAmount(orderItems);
    float totalAmountTextWidth =
        g2.getFontMetrics(boldContentFont)
            .stringWidth("￥" + PriceUtil.calcOrderItemTotalAmount(orderItems));
    g2.drawString(
        "￥" + totalAmount, (float) maxWidth - totalAmountTextWidth - 10, (float) y + line);
    line += heigth;
    g2.drawLine((int) x, (int) (y + line), (int) x + 227, (int) (y + line));
    heigth = contentFont.getSize2D();
    g2.setFont(contentFont);
    line += heigth;
    line += heigth;
    g2.drawString("下单人: 管理员", (float) x + 5, (float) y + line);
    line += heigth;
    g2.drawString(
        "打印时间:" + LocalDateTime.now().format(timeFormatter), (float) x + 5, (float) y + line);

    if (pageIndex == 0) {
      return PAGE_EXISTS;
    }
    return NO_SUCH_PAGE;
    // @formatter:on
  }
}
