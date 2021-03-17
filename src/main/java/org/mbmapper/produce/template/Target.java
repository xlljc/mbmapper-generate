package org.mbmapper.produce.template;

import org.mbmapper.utils.RegexUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;

/**
 * 节点类
 */
public abstract class Target {

    /** 节点名称 */
    private final String targetName;

    public Target(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetName() {
        return targetName;
    }

    /**
     * 从字符串加载Target对象,
     * @param cls 创建的类
     * @param targetString 标签字符串,包含属性, 例如: <#if test="true">
     */
    public static <T extends Target> T Load(Class<T> cls, String targetString) throws MbMapperTemplateException {
        targetString = targetString.trim();
        //名称
        String name = RegexUtil.findFirst("(?<=<#)[\\w]+", targetString);
        if (name == null) {
            throw new MbMapperTemplateException("");
        }
        name = name.toLowerCase();

        T inst = null;
        try {
            Constructor<T> constructor = cls.getDeclaredConstructor(String.class);
            inst = constructor.newInstance(name);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }

        //初始化属性



        return inst;
    }

    @Override
    public String toString() {
        return "Target{" +
                "targetName='" + targetName + '\'' +
                '}';
    }
}
