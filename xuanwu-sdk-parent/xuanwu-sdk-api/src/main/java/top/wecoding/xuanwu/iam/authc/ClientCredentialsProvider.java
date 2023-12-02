package top.wecoding.xuanwu.iam.authc;

/**
 * @author wecoding
 * @since 0.8
 */
@FunctionalInterface
@SuppressWarnings("rawtypes")
public interface ClientCredentialsProvider {

	ClientCredentials resolveCredentials();

}
