package top.wecoding.xuanwu.file.entity;

import lombok.Data;

/**
 * @author wecoding
 * @since 0.9
 */
@Data
public class FileInfo {

  private String attachmentUuid;

  private String directory;

  private String fileUrl;

  private String fileType;

  private String fileName;

  private Long fileSize;

  private String bucketName;

  private String fileKey;

  private Long tenantId;

  private String md5;

  private String storageCode;

  private String sourceType;

  private String serverCode;
}
