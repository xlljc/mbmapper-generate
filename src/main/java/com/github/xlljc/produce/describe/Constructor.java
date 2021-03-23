package com.github.xlljc.produce.describe;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 构造函数
 * 不需要添加无参和全参,因为Class对象可以直接获取
 */
@Data
public class Constructor {

    /**
     * 访问修饰符
     */
    private AccessModify accessModify = AccessModify.PUBLIC;

    /**
     * 返回值类型
     */
    private Type returnType;

    /**
     * 方法名
     */
    private String name;

    /**
     * 参数
     */
    private List<Param> params = new ArrayList<>();

    /**
     * 方法体
     */
    private String body;
}
