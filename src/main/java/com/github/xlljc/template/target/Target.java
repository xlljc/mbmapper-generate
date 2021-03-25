package com.github.xlljc.template.target;

import com.github.xlljc.template.TargetResult;
import com.github.xlljc.utils.RegexUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.regex.Matcher;

/**
 * 节点类
 */
public abstract class Target {

    /** 节点名称 */
    protected final String targetName;

    public Target(String targetName) {
        this.targetName = targetName;
    }

    public String getTargetName() {
        return targetName;
    }

    /**
     * 从字符串加载Target对象,
     * @param cls 创建的类
     * @param targetResult 标签描述
     */
    public static <T extends Target> T Load(Class<T> cls, TargetResult targetResult) throws Exception {
        String name = targetResult.getName().trim().toLowerCase();

        T inst = null;
        try {
            Constructor<T> constructor = cls.getDeclaredConstructor(String.class);
            inst = constructor.newInstance(name);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return null;
        }

        //初始化属性
        String attributeStr = targetResult.getOpenTarget().replaceAll("(^<#[\\w]+\\s*)|(\\s*/?>$)", "");
        Matcher matcher = RegexUtil.matcher("([\\w\\-]+\\s*=\\s*\"[^\\n]*\")|([\\w\\-]+)", attributeStr);
        while (matcher.find()) {
            String attrName = null;
            String value = null;
            //有值
            String a1 = matcher.group(1);
            //无值 (只适用于boolean)
            String a2 = matcher.group(2);
            if (a1 != null) {
                Matcher attrMatcher = RegexUtil.matcher("(^[\\w\\-]+)|((?<=\")[^\\n\"]*(?=\"))", a1);
                attrMatcher.find();
                attrName = attrMatcher.group(1);
                attrMatcher.find();
                value = attrMatcher.group(2);
            } else if (a2 != null) {
                attrName = a2;
                value = "true";
            }
            inst.setValue(attrName, value);
        }

        return inst;
    }

    protected void setValue(String attrName, String value) throws NoSuchFieldException, IllegalAccessException {
        Field field = this.getClass().getDeclaredField(attrName);
        field.setAccessible(true);
        field.set(this, value);
    }

    public abstract String beforeProcess(String content);

    public abstract String process(String content);

}
