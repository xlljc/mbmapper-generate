package com.github.xlljc.template.interfaces;

import java.util.Map;

/**
 * 注册机
 * @param <T>
 */
public interface Registry<T> {

    /**
     * 注册内容
     * @return 名称 - Class对象 键值对
     */
    Map<String, T> registry();

}
