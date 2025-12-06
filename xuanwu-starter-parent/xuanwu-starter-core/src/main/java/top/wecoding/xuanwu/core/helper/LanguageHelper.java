package top.wecoding.xuanwu.core.helper;

import lombok.Getter;
import org.apache.commons.lang3.LocaleUtils;

import java.util.Locale;

/**
 * @author wecoding
 * @since 0.9
 */
public class LanguageHelper {

    @Getter
    private static volatile String defaultLanguage = "zh_CN";

    private LanguageHelper() {
    }

    public static void setDefaultLanguage(String lang) {
        LanguageHelper.defaultLanguage = lang;
    }

    /**
     * 根据当前登陆用户获取语言信息
     * @return String
     */
    public static String language() {
        return defaultLanguage;
    }

    public static Locale locale() {
        return LocaleUtils.toLocale(LanguageHelper.language());
    }

}
