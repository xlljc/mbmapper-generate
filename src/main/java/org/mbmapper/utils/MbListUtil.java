package org.mbmapper.utils;

import java.util.List;

public class MbListUtil {

    /**
     * 将集合拼接为字符串
     */
    public static String join(List<?> list, String join) {
        return join(list, join, null, null);
    }

    /**
     * 将集合拼接为字符串
     */
    public static String join(List<?> list, String join, String before, String append) {
        StringBuilder str = new StringBuilder(before != null ? before : "");
        for (int i = 0; i < list.size(); i++) {
            Object temp = list.get(i);
            if (i > 0) {
                str.append(join);
            }
            str.append(temp.toString());
        }
        return append != null ? str + append : str.toString();
    }

}
