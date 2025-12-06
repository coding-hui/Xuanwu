package top.wecoding.xuanwu.codegen.util;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.io.StringWriter;
import java.util.Map;

/**
 * @author wecoding
 * @since 0.9
 */
public class VelocityRender {

    public static String renderStr(VelocityContext context, String str, Map<String, Object> dataModel) {
        StringWriter stringWriter = new StringWriter();
        Velocity.evaluate(context, stringWriter, "renderStr", str);
        return stringWriter.toString();
    }

}
