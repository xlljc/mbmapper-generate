package org.mbmapper.produce.describe;

import lombok.Data;
import org.mbmapper.config.MbMapperConfig;
import org.mbmapper.produce.table.Table;

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
     * 数据库对应的表
     */
    private Table table;
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
     * 基类
     * key: 类名, value: 导入包
     */
    private Type base;
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
    public String toJavaCode(MbMapperConfig config) {
        if (config.isUseLombok()) {
            imports.add("lombok.Data");
        }

        StringBuilder code = new StringBuilder();
        //包名
        if (packageName != null) {
            code.append("package ").append(packageName).append(";\n");
        }

        //导入的包
        imports.forEach(item -> {
            code.append(String.format("import %s;\n", item));
        });
        //类注释
        if (config.isUseComment()) {
            if (comment != null) {
                code.append(String.format("/**\n * 表名: %s\n * %s\n */\n", table.getName(), comment));
            } else {
                code.append(String.format("/**\n * 表名: %s\n */\n", table.getName()));
            }
        }

        //Lombok相关
        if (config.isUseLombok()) {
            code.append("@Data");
        }
        //类
        code.append(String.format("public %s %s {\n\n", type.getValue(), className));

        //字段
        for (Field field : fields) {
            if (field.getComment() != null) {
                code.append(String.format("\t/** %s */\n\t%s%s %s;\n",
                        field.getComment(), field.getAccessModify().getValue(), field.getType().getName(), field.getName()));
            } else {
                code.append(String.format("\t%s%s %s;\n",
                        field.getAccessModify().getValue(), field.getType().getName(), field.getName()));
            }
        }

        //不使用Lombok就不加构造和 get set 啥的
        if (!config.isUseLombok()) {
            //无参构造
            code.append(getNoArgConstructorCode());
            //全参构造
            code.append(getAllArgsConstructorCode());

            //get,set
            for (Field field : fields) {
                code.append(field.toGetCode(config)).append("\n");
                code.append(field.toSetCode(config)).append("\n");
            }

            //toString
            code.append(getToStringCode());
        }
        return code.append("}").toString();
    }

    /**
     * 获取无参构造函数代码
     */
    private String getNoArgConstructorCode() {
        return String.format("\n\tpublic %s() { }\n", className);
    }

    /**
     * 获取全参构造函数代码
     */
    private String getAllArgsConstructorCode() {
        StringBuilder paramsStr = new StringBuilder();
        StringBuilder bodyStr = new StringBuilder();
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            if (i < fields.size() - 1) {
                paramsStr.append(String.format("%s %s, ", field.getType().getName(), field.getName()));
            } else {
                paramsStr.append(String.format("%s %s", field.getType().getName(), field.getName()));
            }
            bodyStr.append(String.format("\t\tthis.%s = %s;\n", field.getName(), field.getName()));
        }
        return String.format("\tpublic %s(%s) {\n%s\t}\n", className, paramsStr.toString(), bodyStr.toString());
    }

    /**
     * 获取toString代码
     */
    private String getToStringCode() {
        StringBuilder body = new StringBuilder(String.format("\t\treturn \"%s: { \" + \n",className));
        for (int i = 0; i < fields.size(); i++) {
            Field field = fields.get(i);
            if (i < fields.size() - 1) {
                body.append(String.format("\t\t\t\"%s='\" + %s + \"', \" +\n", field.getName(), field.getName()));
            }else {
                body.append(String.format("\t\t\t\"%s='\" + %s + \"'\" +\n", field.getName(), field.getName()));
            }
        }
        body.append("\t\t\t\" }\";");
        return String.format("\t@Override\n\tpublic String toString() {\n%s\n\t}\n", body.toString());
    }
}
