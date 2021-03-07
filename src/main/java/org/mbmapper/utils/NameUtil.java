package org.mbmapper.utils;

import java.util.regex.Matcher;

public class NameUtil {

    /**
     * 将数据库字段名转成驼峰命名
     *
     * @param name 名称
     */
    public static String toHumpName(String name) {
        Matcher matcher = RegexUtil.matcher("(?<=\\w)_(\\w)", name);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString().replace("_", "");
    }

    /**
     * 将名称首字母大写
     *
     * @param name 名称
     */
    public static String firstToUpperCase(String name) {
        return name.replaceFirst("\\w", name.substring(0, 1).toUpperCase());
    }

    /**
     * 将名称首字母小写
     *
     * @param name 名称
     */
    public static String firstToLowerCase(String name) {
        return name.replaceFirst("\\w", name.substring(0, 1).toLowerCase());
    }

}
