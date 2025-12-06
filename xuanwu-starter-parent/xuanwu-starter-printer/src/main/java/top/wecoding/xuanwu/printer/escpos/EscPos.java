package top.wecoding.xuanwu.printer.escpos;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.ObjectUtils;
import top.wecoding.xuanwu.core.exception.SystemErrorCode;
import top.wecoding.xuanwu.core.util.ArgumentAssert;
import top.wecoding.xuanwu.printer.escpos.params.BarCode;
import top.wecoding.xuanwu.printer.escpos.params.Constant;
import top.wecoding.xuanwu.printer.escpos.params.Goods;
import top.wecoding.xuanwu.printer.escpos.params.PosParam;
import top.wecoding.xuanwu.printer.escpos.params.PosTpl;
import top.wecoding.xuanwu.printer.escpos.params.QrCode;
import top.wecoding.xuanwu.printer.escpos.params.Text;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 控制打印机工具类
 *
 * @author wecoding
 * @since 0.9
 */
public class EscPos {

    // 以ip作为key，EscPos实例作为value的Map
    private static Map<String, EscPos> posMap = new HashMap<String, EscPos>();

    private static EscPos escPos = null;

    private static String encoding = null;

    // 通过socket流进行读写
    private OutputStream socketOut;

    private OutputStreamWriter writer;

    /**
     * 根据ip、端口、字符编码构造工具类实例
     * @param ip 打印机ip
     * @param port 打印机端口，默认9100
     * @param encoding 打印机支持的编码格式(主要针对中文)
     * @throws IOException
     */
    public EscPos(String ip, int port, String encoding) throws IOException {
        Socket socket = new Socket(ip, port);
        this.socketOut = socket.getOutputStream();
        socket.isClosed();
        this.encoding = encoding;
        this.writer = new OutputStreamWriter(socketOut, StandardCharsets.ISO_8859_1);
    }

    public synchronized static EscPos getInstance(String ip, Integer port, String encoding) throws IOException {
        escPos = posMap.get(ip);
        if (escPos == null) {
            escPos = new EscPos(ip, port, encoding);
        }
        return escPos;
    }

    public synchronized static EscPos getInstance(String ip, Integer port) throws IOException {
        return getInstance(ip, port, Constant.DEFAULT_ENCODING);
    }

    public static synchronized EscPos getInstance(String ip) throws IOException {
        return getInstance(ip, Constant.DEFAULT_PORT, Constant.DEFAULT_ENCODING);
    }

    /**
     * 根据某班内容和参数打印小票
     * @param template 模板内容
     * @param param 参数
     * @throws IOException
     */
    public static void print(String template, String param) throws IOException {
        PosParam posParam = JSON.parseObject(param, PosParam.class);

        Map<String, Object> keyMap = posParam.getKeys();
        List<Map<String, Object>> goodsParam = posParam.getGoods();

        // replace placeholder in template
        Pattern pattern = Pattern.compile(Constant.REPLACE_PATTERN);

        Matcher matcher = pattern.matcher(template);
        StringBuilder sb = new StringBuilder();

        while (matcher.find()) {
            String key = matcher.group(1);
            Object value = keyMap.get(key);
            value = Objects.isNull(value) ? "" : value.toString();
            matcher.appendReplacement(sb, value.toString());
        }

        matcher.appendTail(sb);

        template = sb.toString();

        PosTpl posTpl = JSON.parseObject(template, PosTpl.class);

        // print header
        for (JSONObject jsonObject : posTpl.getHeader()) {
            print(jsonObject);
        }

        // print goods
        // print title
        for (Goods goods : posTpl.getGoods()) {
            printTitle(goods);
        }
        escPos.line(1);

        // print detail
        for (Map<String, Object> goods : goodsParam) {
            printGoods(goods, posTpl.getGoods());
        }

        // print bill
        for (JSONObject jsonObject : posTpl.getBill()) {
            print(jsonObject);
        }

        // print footer
        for (JSONObject jsonObject : posTpl.getFooter()) {
            print(jsonObject);
        }

        escPos.line(2);

        escPos.feedAndCut();
    }

    /**
     * 打印任何对象
     * @param jsonObject 需要输出对象
     * @throws IOException ex
     */
    private static void print(JSONObject jsonObject) throws IOException {
        int type = jsonObject.getInteger("type");

        switch (type) {
            case 0 -> {
                Text text = JSON.toJavaObject(jsonObject, Text.class);
                printText(text);
            }
            case 1 -> {
                BarCode barCode = JSON.toJavaObject(jsonObject, BarCode.class);
                printBarCode(barCode);
            }
            case 2 -> {
                QrCode qrCode = JSON.toJavaObject(jsonObject, QrCode.class);
                printQrCode(qrCode);
            }
            default -> ArgumentAssert.error(SystemErrorCode.PARAM_ERROR, "不支持的打印类型");
        }
    }

    /**
     * 打印纯文本
     * @param text 文本内容
     * @throws IOException ex
     */
    private static void printText(Text text) throws IOException {
        escPos.align(text.getFormat())
            .bold(text.isBold())
            .underline(text.isUnderline())
            .size(text.getSize())
            .printStr(text.getText())
            .boldOff(text.isBold())
            .underlineOff(text.isUnderline())
            .sizeReset()
            .line(text.getLine());
    }

    /**
     * 打印条形码
     * @param barCode 条形码内容
     * @throws IOException ex
     */
    private static void printBarCode(BarCode barCode) throws IOException {
        escPos.align(barCode.getFormat()).barCode(barCode.getText()).line(barCode.getLine());
    }

    /**
     * 打印二维码
     * @param qrCode 二维码内容
     * @throws IOException ex
     */
    private static void printQrCode(QrCode qrCode) throws IOException {
        escPos.qrCode(qrCode.getFormat(), qrCode.getText()).line(qrCode.getLine());
    }

    /**
     * 打印商品小票的列名
     * @param goods 商品
     * @throws IOException ex
     */
    private static void printTitle(Goods goods) throws IOException {
        escPos.align(goods.getFormat())
            .bold(false)
            .underline(false)
            .size(6)
            .printStr(fillLength(goods.getName(), goods))
            .boldOff(false)
            .underlineOff(false)
            .sizeReset()
            .line(0);
    }

    /**
     * 循环打印商品信息
     * @param goods
     * @param goodsList
     * @throws IOException
     */
    private static void printGoods(Map<String, Object> goods, List<Goods> goodsList) throws IOException {
        for (Goods ele : goodsList) {
            escPos.align(ele.getFormat())
                .bold(false)
                .underline(false)
                .size(1)
                .printStr(fillLength(goods.get(ele.getVariable()).toString(), ele))
                .boldOff(false)
                .underlineOff(false)
                .line(0);
        }
        escPos.line(2);
    }

    /**
     * 填充打印文本长度
     * @param str
     * @param goods
     * @return
     */
    private static String fillLength(String str, Goods goods) {
        try {
            int width = goods.getWidth();
            int length = str.getBytes(encoding).length;
            switch (goods.getFormat()) {
                case 0 -> {
                    while (length < width) {
                        str += " ";
                        length++;
                    }
                }
                case 1 -> {
                    if (length < width) {
                        String text = "";
                        int pre = (width - length) / 2;
                        int end = width - length - pre;
                        while (pre > 0) {
                            text += " ";
                            pre--;
                        }
                        while (end > 0) {
                            str += " ";
                            end--;
                        }
                        str = text + str;
                    }
                }
                case 2 -> {
                    String text = "";
                    while (length < width) {
                        text += " ";
                        length++;
                    }
                    str = text + str;
                }
                default -> {
                }
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 换行
     * @param lineNum 换行数，0为不换行
     * @return
     * @throws IOException
     */
    private EscPos line(int lineNum) throws IOException {
        for (int i = 0; i < lineNum; i++) {
            writer.write("\n");
            writer.flush();
        }
        return this;
    }

    /**
     * 下划线
     * @param flag false为不添加下划线
     * @return
     * @throws IOException
     */
    private EscPos underline(boolean flag) throws IOException {
        if (flag) {
            writer.write(0x1B);
            writer.write(45);
            writer.write(2);
        }
        return this;
    }

    /**
     * 取消下划线
     * @param flag true为取消下划线
     * @return
     * @throws IOException
     */
    private EscPos underlineOff(boolean flag) throws IOException {
        if (flag) {
            writer.write(0x1B);
            writer.write(45);
            writer.write(0);
        }
        return this;
    }

    /**
     * 加粗
     * @param flag false为不加粗
     * @return
     * @throws IOException
     */
    private EscPos bold(boolean flag) throws IOException {
        if (flag) {
            writer.write(0x1B);
            writer.write(69);
            writer.write(0xF);
        }
        return this;
    }

    /**
     * 取消粗体
     * @param flag true为取消粗体模式
     * @return
     * @throws IOException
     */
    private EscPos boldOff(boolean flag) throws IOException {
        if (flag) {
            writer.write(0x1B);
            writer.write(69);
            writer.write(0);
        }
        return this;
    }

    /**
     * 排版
     * @param position 0：居左(默认) 1：居中 2：居右
     * @return
     * @throws IOException
     */
    private EscPos align(int position) throws IOException {
        writer.write(0x1B);
        writer.write(97);
        writer.write(position);
        return this;
    }

    /**
     * 初始化打印机
     * @return
     * @throws IOException
     */
    private EscPos init() throws IOException {
        writer.write(0x1B);
        writer.write(0x40);
        return this;
    }

    /**
     * 二维码排版对齐方式
     * @param position 0：居左(默认) 1：居中 2：居右
     * @param moduleSize 二维码version大小
     * @return
     * @throws IOException
     */
    private EscPos alignQr(int position, int moduleSize) throws IOException {
        writer.write(0x1B);
        writer.write(97);
        if (position == 1) {
            writer.write(1);
            centerQr(moduleSize);
        }
        else if (position == 2) {
            writer.write(2);
            rightQr(moduleSize);
        }
        else {
            writer.write(0);
        }
        return this;
    }

    /**
     * 居中牌排列
     * @param moduleSize 二维码version大小
     * @throws IOException
     */
    private void centerQr(int moduleSize) throws IOException {
        switch (moduleSize) {
            case 1: {
                printSpace(16);
                break;
            }
            case 2: {
                printSpace(18);
                break;
            }
            case 3: {
                printSpace(20);
                break;
            }
            case 4: {
                printSpace(22);
                break;
            }
            case 5: {
                printSpace(24);
                break;
            }
            case 6: {
                printSpace(26);
                break;
            }
            default:
                break;
        }
    }

    /**
     * 二维码居右排列
     * @param moduleSize 二维码version大小
     * @throws IOException
     */
    private void rightQr(int moduleSize) throws IOException {
        switch (moduleSize) {
            case 1 -> printSpace(14);
            case 2 -> printSpace(17);
            case 3 -> printSpace(20);
            case 4 -> printSpace(23);
            case 5 -> printSpace(26);
            case 6 -> printSpace(28);
            default -> {
            }
        }
    }

    /**
     * 打印空白
     * @param length 需要打印空白的长度
     * @throws IOException
     */
    private void printSpace(int length) throws IOException {
        for (int i = 0; i < length; i++) {
            writer.write(" ");
        }
        writer.flush();
    }

    /**
     * 字体大小
     * @param size 1-8 选择字号
     * @return
     * @throws IOException
     */
    private EscPos size(int size) throws IOException {
        int fontSize = switch (size) {
            case 1 -> 0;
            case 2 -> 17;
            case 3 -> 34;
            case 4 -> 51;
            case 5 -> 68;
            case 6 -> 85;
            case 7 -> 102;
            case 8 -> 119;
            default -> 0;
        };
        writer.write(0x1D);
        writer.write(33);
        writer.write(fontSize);
        return this;
    }

    /**
     * 重置字体大小
     * @return
     * @throws IOException
     */
    private EscPos sizeReset() throws IOException {
        writer.write(0x1B);
        writer.write(33);
        writer.write(0);
        return this;
    }

    /**
     * 进纸并全部切割
     * @return
     * @throws IOException
     */
    private EscPos feedAndCut() throws IOException {
        writer.write(0x1D);
        writer.write(86);
        writer.write(65);
        writer.write(0);
        writer.flush();
        return this;
    }

    /**
     * 打印条形码
     * @param value
     * @return
     * @throws IOException
     */
    private EscPos barCode(String value) throws IOException {
        writer.write(0x1D);
        writer.write(107);
        writer.write(67);
        writer.write(value.length());
        writer.write(value);
        writer.flush();
        return this;
    }

    /**
     * 打印二维码
     * @param qrData
     * @return
     * @throws IOException
     */
    private EscPos qrCode(int position, String qrData) throws IOException {
        int moduleSize = 0;
        int length = qrData.getBytes(encoding).length;
        int l = (int) (Math.ceil(1.5 * length) * 8);
        if (l < 200) {
            moduleSize = 1;
        }
        else if (l < 429) {
            moduleSize = 2;
        }
        else if (l < 641) {
            moduleSize = 3;
        }
        else if (l < 885) {
            moduleSize = 4;
        }
        else if (l < 1161) {
            moduleSize = 5;
        }
        else if (l < 1469) {
            moduleSize = 6;
        }

        alignQr(position, moduleSize);

        writer.write(0x1D);// init
        writer.write("(k");// adjust height of barcode
        writer.write(length + 3); // pl
        writer.write(0); // ph
        writer.write(49); // cn
        writer.write(80); // fn
        writer.write(48); //
        writer.write(qrData);

        writer.write(0x1D);
        writer.write("(k");
        writer.write(3);
        writer.write(0);
        writer.write(49);
        writer.write(69);
        writer.write(48);

        writer.write(0x1D);
        writer.write("(k");
        writer.write(3);
        writer.write(0);
        writer.write(49);
        writer.write(67);
        writer.write(moduleSize);

        writer.write(0x1D);
        writer.write("(k");
        writer.write(3); // pl
        writer.write(0); // ph
        writer.write(49); // cn
        writer.write(81); // fn
        writer.write(48); // m

        writer.flush();

        return this;
    }

    private void write(byte... data) throws IOException {
        socketOut.write(data);
        socketOut.flush();
    }

    /**
     * 打印字符串
     * @param str 所需打印字符串
     * @return
     * @throws IOException
     */
    private EscPos printStr(String str) throws IOException {
        writer.write(str);
        writer.flush();
        return this;
    }

}
