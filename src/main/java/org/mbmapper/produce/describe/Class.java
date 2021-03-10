package org.mbmapper.produce.describe;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
public class Class {

    /**
     * 包名
     */
    private String packageName;
    /**
     * 导入的包
     */
    private List<String> imports = new ArrayList<>();
    /**
     * 注释
     */
    private String comment;
    /**
     * 注解
     */
    private Map<String, Annotation> annotations;
    /**
     * 访问修饰符
     */
    private AccessModify accessModify = AccessModify.PUBLIC;
    /**
     * 类型
     */
    private DefineType type = DefineType.CLASS;
    /**
     * 类名
     */
    private String className;
    /**
     * 数据库表名
     */
    private String tableName;
    /**
     * 基类
     * key: 类名, value: 导入包
     */
    private KeyValue<String, String> base;
    /**
     * 字段
     */
    private List<Field> fields;
    /**
     * 构造函数
     */
    private List<Constructor> constructors;
    /**
     * 方法函数
     */
    private List<Method> methods;

    /**
     * 添加导入的包或类
     *
     * @param importClass 包名或类名
     */
    public void addImport(String importClass) {
        if (!imports.contains(importClass)) {
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
        //导入的包
        imports.forEach(item -> {
            code.append(String.format("import %s;\n", item));
        });
        //类
        code.append(String.format("public %s %s {\n\t\t\n", type.getValue(), className));

        //字段
        for (Field field : fields) {
            code.append(String.format("\t%s%s %s;\n", field.getAccessModify().getValue(), field.getType(), field.getName()));
        }
        //get,set
        for (Field field : fields) {
            code.append(field.toGetCode()).append("\n");
            code.append(field.toSetCode()).append("\n");
        }

        return code.append("}").toString();
    }

}
