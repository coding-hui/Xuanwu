package top.wecoding.xuanwu.codegen.util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author wecoding
 * @since 0.9
 */
@Slf4j
public class DownloadUtil {

	public static void download(HttpServletRequest request, HttpServletResponse response, String name, byte[] bytes) {
		response.setCharacterEncoding(request.getCharacterEncoding());
		response.setContentType("application/octet-stream");
		try {
			response.setHeader("Content-Disposition", "attachment; filename=" + name);
			StreamUtils.copy(bytes, response.getOutputStream());
			response.flushBuffer();
		}
		catch (Exception e) {
			log.error("failed to download: {}", e.getMessage(), e);
		}
	}

	public static void downloadFile(HttpServletRequest request, HttpServletResponse response, File file,
			boolean deleteOnExit) {
		response.setCharacterEncoding(request.getCharacterEncoding());
		response.setContentType("application/octet-stream");
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
			StreamUtils.copy(fis, response.getOutputStream());
			response.flushBuffer();
		}
		catch (Exception e) {
			log.error("failed to download: {}", e.getMessage(), e);
		}
		finally {
			if (fis != null) {
				try {
					fis.close();
					if (deleteOnExit) {
						file.deleteOnExit();
					}
				}
				catch (IOException e) {
					log.error(e.getMessage(), e);
				}
			}
		}
	}

}
