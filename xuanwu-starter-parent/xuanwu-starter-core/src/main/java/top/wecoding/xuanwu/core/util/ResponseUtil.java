package top.wecoding.xuanwu.core.util;

import static top.wecoding.xuanwu.core.constant.StrPool.UTF8;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import top.wecoding.xuanwu.core.base.R;
import top.wecoding.xuanwu.core.exception.ErrorCode;

/**
 * @author liuyuhui
 * @date 2022/9/12
 */
public class ResponseUtil {

  public static void webMvcResponseWriter(
      HttpServletResponse response, HttpStatus status, ErrorCode codeSupplier) throws IOException {
    response.setStatus(status.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(UTF8);
    R<Object> result = R.error(codeSupplier, status.getReasonPhrase());
    String errorStr = JsonUtil.toJsonStr(result);
    try (PrintWriter writer = response.getWriter()) {
      writer.print(errorStr);
    }
  }

  public static void webMvcResponseWriter(
      HttpServletResponse response, HttpStatus status, ErrorCode codeSupplier, String message)
      throws IOException {
    response.setStatus(status.value());
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding(UTF8);
    R<Object> result = R.error(codeSupplier, message);
    String errorStr = JsonUtil.toJsonStr(result);
    try (PrintWriter writer = response.getWriter()) {
      writer.print(errorStr);
    }
  }
}
