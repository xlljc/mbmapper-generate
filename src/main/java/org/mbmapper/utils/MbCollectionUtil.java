package org.mbmapper.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MbCollectionUtil {

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

    /**
     * 在list中查找指定的项
     */
    public static <T> T find(List<T> list,MbListFind<T> callback) {
        for (T item : list) {
            if (callback.find(item)) return item;
        }
        return null;
    }

    /**
     * 在list中查找指定的项的索引
     */
    public static <T> int findIndex(List<T> list,MbListFind<T> callback) {
        for (int i = 0; i < list.size(); i++) {
            if (callback.find(list.get(i))) return i;
        }
        return -1;
    }

    /**
     * 将一个字符串装换成字符串集合
     * @param str 字符串
     */
    public static List<String> parseList(String str) {
        return new ArrayList<>(Arrays.asList(
                str.replaceAll("(\\[\\s*,*)|(,*\\s*])|((?<=[\\[,])\\s+)|(\\s+(?=[],]))", "").split("(?<!\\\\),")));
    }

}
