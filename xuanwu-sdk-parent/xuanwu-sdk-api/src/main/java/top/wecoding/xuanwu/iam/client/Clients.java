package top.wecoding.xuanwu.iam.client;

import top.wecoding.xuanwu.core.util.Classes;

/**
 * @author Wecoding Liu
 * @since 0.8
 */
public class Clients {

    public static ClientBuilder builder() {
        return Classes.newInstance("top.wecoding.xuanwu.iam.client.DefaultClientBuilder");
    }

}
