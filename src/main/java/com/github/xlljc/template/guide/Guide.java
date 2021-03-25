package com.github.xlljc.template.guide;

/**
 * 向导类
 */
public interface Guide<T> {

    /**
     * 注册内容
     *
     * @param registry 注册机对象
     */
    void registry(Registry<T> registry);

    /**
     * 注册多个内容
     *
     * @param registries 注册机对象,数组
     */
    void registries(Registry<T>[] registries);

    /**
     * 根据描述获取内容
     *
     * @param describe 描述
     */
    T find(String describe);

}
