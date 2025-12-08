package top.wecoding.xuanwu.file.service;

/**
 * @author wecoding
 * @since 0.9
 */
public interface StoreFactory {

  Integer type();

  AbstractFileService create();
}
