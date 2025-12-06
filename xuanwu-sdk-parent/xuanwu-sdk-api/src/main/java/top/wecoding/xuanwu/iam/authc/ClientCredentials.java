package top.wecoding.xuanwu.iam.authc;

/**
 * @author wecoding
 * @since 0.8
 */
@FunctionalInterface
public interface ClientCredentials<T> {

    T getCredentials();

}
