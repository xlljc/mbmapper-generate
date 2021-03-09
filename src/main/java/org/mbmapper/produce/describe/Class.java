package org.mbmapper.produce.describe;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class Class {

    /** 包名 */
    private String packageName;
    /** 导入的包 */
    private List<String> imports = new ArrayList<>();
    /** 注释 */
    private String comment;
    /** 注解 */
    private Map<String, Annotation> annotations;
    /** 访问修饰符 */
    private AccessModify accessModify = AccessModify.PUBLIC;
    /** 类型 */
    private DefineType type = DefineType.CLASS;
    /** 类名 */
    private String className;
    /** 数据库表名 */
    private String tableName;
    /** 基类 */
    private KeyValue<String, String> base;
    /** 字段 */
    private Map<String, Field> fields;
    /** 构造函数 */
    private Map<String, Constructor> constructors;
    /** 方法函数 */
    private Map<String, Method> methods;

    /**
     * 添加导入的包或类
     * @param importClass 包名或类名
     */
    public void addImport(String importClass) {
        if (!imports.contains(importClass)){
            imports.add(importClass);
        }
    }

    /**
     * 将该类转成java代码
     */
    public String toJavaCode() {
        StringBuilder code = new StringBuilder();

        //包名
        if (packageName != null) {
            code.append("package ").append(packageName).append(";\n");
        }
        //类
        if (className != null) {
            code.append(String.format("public %s %s {\n\t\t\n}",type.getValue(),className));
        }

        return code.toString();
    }

}
