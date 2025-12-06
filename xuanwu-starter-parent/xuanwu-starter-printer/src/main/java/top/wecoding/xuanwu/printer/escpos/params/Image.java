package top.wecoding.xuanwu.printer.escpos.params;

import lombok.Data;

/**
 * 图片配置参数
 * <p>
 * 热敏打印机一般只能打印黑白 所以图片是经过二值化处理后的
 *
 * @author wecoding
 * @since 0.9
 */
@Data
public class Image {

    /**
     * 打印内容类型
     */
    private int type;

    /**
     * 对齐方式 居左、居中、居右
     */
    private int format;

    /**
     * 空行行数
     */
    private int line;

    /**
     * 图片绝对路径
     */
    private String path;

}
