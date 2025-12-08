package top.wecoding.xuanwu.core.util;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author liuyuhui
 * @since 0.5.2
 */
public class WebUtil extends org.springframework.web.util.WebUtils {

  private static final List<MediaType> MEDIA_TYPES_ALL = Collections.singletonList(MediaType.ALL);

  /**
   * Accept 包含 TEXT_HTML
   *
   * @param request {@link HttpServletRequest}
   * @return {@link Boolean}
   */
  public static boolean acceptIncludeTextHtml(HttpServletRequest request) {
    for (MediaType mediaType : getAcceptedMediaTypes(request)) {
      if (mediaType.includes(MediaType.TEXT_HTML)) {
        return true;
      }
    }
    return false;
  }

  /**
   * 获取 MediaType List
   *
   * @param request {@link HttpServletRequest}
   * @return {@link List <MediaType>}
   */
  public static List<MediaType> getAcceptedMediaTypes(HttpServletRequest request) {
    String acceptHeader = request.getHeader(HttpHeaders.ACCEPT);
    if (org.springframework.util.StringUtils.hasText(acceptHeader)) {
      return MediaType.parseMediaTypes(acceptHeader);
    }
    return MEDIA_TYPES_ALL;
  }

  public static ServletRequestAttributes getServletRequestAttributes() {
    return (ServletRequestAttributes)
        Objects.requireNonNull(RequestContextHolder.getRequestAttributes());
  }

  public static HttpServletRequest getRequest() {
    return getServletRequestAttributes().getRequest();
  }

  public static HttpServletResponse getResponse() {
    return getServletRequestAttributes().getResponse();
  }
}
