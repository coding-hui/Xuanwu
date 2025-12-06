package top.wecoding.xuanwu.iam.common.config;

import lombok.val;
import org.junit.jupiter.api.Test;
import top.wecoding.xuanwu.iam.common.io.TestStringResource;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author wecoding
 * @since 0.8
 */
class YamlPropertiesSourceTest {

    @Test
    void testGetProperties() {
        String testStr = "wecoding:\n  client:\n    apiBase: xxx\n";
        val properties = new YamlPropertiesSource(new TestStringResource(testStr));
        assertEquals(properties.getProperties().get("wecoding.client.apiBase"), "xxx");
    }

}