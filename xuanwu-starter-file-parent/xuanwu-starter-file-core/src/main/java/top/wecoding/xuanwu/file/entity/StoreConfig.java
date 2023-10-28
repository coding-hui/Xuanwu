package top.wecoding.xuanwu.file.entity;

import lombok.Data;

/**
 * @author wecoding
 * @since 0.9
 */
@Data
public class StoreConfig {

	private Integer storageType;

	private String domain;

	private String endPoint;

	private String accessKeyId;

	private String accessKeySecret;

	private Integer appId;

	private String region;

	private Integer defaultFlag;

	private Long tenantId;

	private String accessControl;

	private String bucketPrefix;

	private String storageCode;

	private String prefixStrategy;

	private Integer createBucketFlag;

}
