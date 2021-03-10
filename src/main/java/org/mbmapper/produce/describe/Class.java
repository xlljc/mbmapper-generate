package org.mbmapper.produce.describe;

import lombok.Data;
import org.mbmapper.config.MbMapperConfig;
import org.mbmapper.produce.table.Table;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Class implements Serializable {

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
    private Map<String, Annotation> annotations = new HashMap<>();
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
     * 实现的接口
     */
    private List<Type> implementList = new ArrayList<>();
    /**
     * 字段
     */
    private List<Field> fields = new ArrayList<>();
    /**
     * 构造函数
     */
    private List<Constructor> constructors = new ArrayList<>();
    /**
     * 方法函数
     */
    private List<Method> methods = new ArrayList<>();

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

        //Lombok相关
        if (config.isUseLombok()) {
            Annotation annotation = new Annotation();
            annotation.setType(new Type("Data", "lombok.Data"));
            annotations.put(annotation.getType().getName(),annotation);
        }

        StringBuilder codeHead = new StringBuilder();
        StringBuilder code = new StringBuilder();

        //类注释
        if (config.isUseComment()) {
            if (comment != null) {
                code.append(String.format("/**\n * 表名: %s\n * %s\n */\n", table.getName(), comment));
            } else {
                code.append(String.format("/**\n * 表名: %s\n */\n", table.getName()));
            }
        }

        //类注解
        annotations.forEach((key, annotation) -> {
            System.out.println("annotation: " + annotation);
            imports.add(annotation.getType().getImportPackage());
            List<KeyValue<String, String>> params = annotation.getParams();
            if (params.size() == 0) {
                code.append(String.format("@%s\n", annotation.getType().getName()));
            } else {
                StringBuilder value = new StringBuilder();
                for (int i = 0; i < params.size(); i++) {
                    KeyValue<String, String> keyValue = params.get(i);
                    if (i > 0) {
                        value.append(", ");
                    }
                    value.append(String.format("%s = %s", keyValue.getKey(), keyValue.getValue()));
                }
                code.append(String.format("@%s(%s)\n", annotation.getType().getName(), value.toString()));
            }
        });

        //类
        code.append(String.format("%s%s %s ",accessModify.getValue(), type.getValue(), className));

        //基类
        if (base != null) {
            code.append("extends ").append(base.getName()).append(" ");
            if (base.getImportPackage() != null) {
                imports.add(base.getImportPackage());
            }
        }
        //实现接口
        for (int i = 0; i < implementList.size(); i++) {
            if (i == 0) {
                code.append("implements ");
            }
            Type temp = implementList.get(i);
            code.append(temp.getName());
            if (i < implementList.size() - 1) {
                code.append(", ");
            } else {
                code.append(" ");
            }
            if (temp.getImportPackage() != null) {
                imports.add(temp.getImportPackage());
            }
        }

        code.append("{\n\n");
        code.append(getBodyCode(config));
        code.append("}");

        //包名
        if (packageName != null) {
            codeHead.append("package ").append(packageName).append(";\n");
        }
        //导入的包
        imports.forEach(item -> {
            codeHead.append(String.format("import %s;\n", item));
        });
        return codeHead.append(code).toString();
    }

    /**
     * 获取class主体代码
     */
    private String getBodyCode(MbMapperConfig config) {
        StringBuilder code = new StringBuilder();
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
        return code.toString();
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
