package com.github.xlljc.produce.describe;

import com.github.xlljc.config.MbMapperConfig;
import com.github.xlljc.produce.table.Column;
import lombok.Data;
import com.github.xlljc.utils.NameUtil;

import java.util.HashMap;
import java.util.Map;

@Data
public class Field {

    /**
     * 字段名
     */
    private String name;
    /**
     * 数据库对应列
     */
    private Column column;
    /**
     * 访问修饰符
     */
    private AccessModify accessModify = AccessModify.PUBLIC;
    /**
     * 类型
     */
    private Type type;
    /**
     * 默认值
     */
    private String defaultValue;
    /**
     * 注释
     */
    private String comment;
    /**
     * 注解
     */
    private Map<String, Annotation> annotations = new HashMap<>();

    /**
     * 获取get代码
     */
    public String toGetCode(MbMapperConfig config) {
        if ("boolean".equals(type.getName()) || "Boolean".equals(type.getName())) {
            if (config.isUseComment() && comment != null) {
                return String.format(
                        "\t/** 数据对应库字段:%s，%s */\n\t%s%s is%s() {\n\t\treturn %s;\n\t}",
                        column.getName(),comment,accessModify.getValue(), type.getName(), NameUtil.firstToUpperCase(name), name);
            }
            return String.format(
                    "\t%s%s is%s() {\n\t\treturn %s;\n\t}",
                    accessModify.getValue(), type.getName(), NameUtil.firstToUpperCase(name), name);
        }
        if (comment != null) {
            return String.format(
                    "\t/** 数据对应库字段:%s，%s */\n\t%s%s get%s() {\n\t\treturn %s;\n\t}",
                    column.getName(),comment,accessModify.getValue(), type.getName(), NameUtil.firstToUpperCase(name), name);
        }
        return String.format(
                "\t%s%s get%s() {\n\t\treturn %s;\n\t}",
                accessModify.getValue(), type.getName(), NameUtil.firstToUpperCase(name), name);
    }

    /**
     * 获取set代码
     */
    public String toSetCode(MbMapperConfig config) {
        if (config.isUseComment() && comment != null) {
            return String.format(
                    "\t/** 数据对应库字段:%s，%s */\n\t%svoid set%s(%s %s) {\n\t\tthis.%s = %s;\n\t}",
                    column.getName(),comment,accessModify.getValue(), NameUtil.firstToUpperCase(name), type.getName(), name, name, name);
        }
        return String.format(
                "\t%svoid set%s(%s %s) {\n\t\tthis.%s = %s;\n\t}",
                accessModify.getValue(), NameUtil.firstToUpperCase(name), type.getName(), name, name, name);
    }

}
