package top.wecoding.xuanwu.file.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import top.wecoding.xuanwu.core.constant.Constant;
import top.wecoding.xuanwu.file.entity.FileInfo;
import top.wecoding.xuanwu.file.entity.StoreConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;

/**
 * @author wecoding
 * @since 0.9
 */
public abstract class AbstractFileService {

	protected StoreConfig config;

	public AbstractFileService init(StoreConfig config) {
		this.config = config;
		return this;
	}

	public void shutdown() {
	}

	public abstract String upload(FileInfo file, String filePath);

	public abstract String upload(FileInfo file, InputStream inputStream);

	public abstract String copy(FileInfo file, String oldFileKey, String oldBucketName);

	public abstract void delete(String bucketName, String url, String fileKey);

	public abstract String getSignedUrl(HttpServletRequest servletRequest, String bucketName, String url,
			String fileKey, String fileName, boolean download, Long expires);

	public abstract void download(HttpServletRequest request, HttpServletResponse response, String bucketName,
			String url, String fileKey);

	public abstract void decryptDownload(HttpServletRequest request, HttpServletResponse response, String bucketName,
			String url, String fileKey, String password);

	public abstract String getObjectPrefixUrl(String realBucketName);

	protected void buildResponse(HttpServletResponse response, byte[] data, String fileName) throws IOException {
		response.reset();
		response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
		response.setContentType("multipart/form-data");
		response.addHeader("Content-Length", "" + data.length);
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		response.setDateHeader("Expires", (System.currentTimeMillis() + 1000));
		// IOU.write(data, response.getOutputStream());
	}

	protected String getRealBucketName(String bucketName) {
		return StringUtils.isNotBlank(config.getBucketPrefix())
				? String.format("%s-%s", config.getBucketPrefix(), bucketName) : bucketName;
	}

	protected String getFileKey(String bucketName, String url) {
		String prefixUrl = getObjectPrefixUrl(bucketName);
		try {
			return URLDecoder.decode(url, Constant.DEFAULT_CHARSET).substring(prefixUrl.length());
		}
		catch (Exception e) {
			return url.substring(prefixUrl.length());
		}
	}

}
