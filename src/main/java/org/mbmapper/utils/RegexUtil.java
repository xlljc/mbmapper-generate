package org.mbmapper.utils;

import java.util.regex.Pattern;

/**
 * 正则表达式验证工具类
 */
public class RegexUtil {

    /**
     * 通过正则表达式验证字符串
     * @param regex 正则表达式
     * @param str 需要验证的字符串
     * @return 是否匹配
     */
    public static boolean matches(String regex,String str) {
        return Pattern.compile(regex).matcher(str).matches();
    }

}
