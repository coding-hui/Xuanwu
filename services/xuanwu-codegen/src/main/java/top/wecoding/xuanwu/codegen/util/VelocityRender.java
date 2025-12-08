package top.wecoding.xuanwu.codegen.util;

import java.io.StringWriter;
import java.util.Map;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

/**
 * @author wecoding
 * @since 0.9
 */
public class VelocityRender {

  public static String renderStr(
      VelocityContext context, String str, Map<String, Object> dataModel) {
    StringWriter stringWriter = new StringWriter();
    Velocity.evaluate(context, stringWriter, "renderStr", str);
    return stringWriter.toString();
  }
}
