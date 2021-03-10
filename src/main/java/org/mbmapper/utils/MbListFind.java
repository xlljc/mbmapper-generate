package org.mbmapper.utils;

/**
 * 搜索回调函数
 */
public interface MbListFind<T> {
    boolean find(T item);
}
