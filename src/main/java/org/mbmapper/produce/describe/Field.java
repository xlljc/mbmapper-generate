package org.mbmapper.produce.describe;

import lombok.Data;
import org.mbmapper.utils.NameUtil;

import java.util.Map;

@Data
public class Field {

    /**
     * 字段名
     */
    private String name;
    /**
     * 数据库列名
     */
    private String columnName;
    /**
     * 访问修饰符
     */
    private AccessModify accessModify = AccessModify.PUBLIC;
    /**
     * 类型
     */
    private String type;
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
    private Map<String, Annotation> annotations;

    /**
     * 获取get代码
     */
    public String toGetCode() {
        if ("boolean".equals(type) || "Boolean".equals(type)) {
            return String.format(
                    "\t%s%s is%s() {\n\t\treturn %s;\n\t}",
                    accessModify.getValue(), type, NameUtil.firstToUpperCase(name), name);
        }
        return String.format(
                "\t%s%s get%s() {\n\t\treturn %s;\n\t}",
                accessModify.getValue(), type, NameUtil.firstToUpperCase(name), name);
    }

    /**
     * 获取set代码
     */
    public String toSetCode() {
        return String.format(
                "\t%svoid set%s(%s %s) {\n\t\tthis.%s = %s;\n\t}",
                accessModify.getValue(), NameUtil.firstToUpperCase(name), type, name, name, name);
    }

}
