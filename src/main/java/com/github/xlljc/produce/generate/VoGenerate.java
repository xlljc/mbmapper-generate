package com.github.xlljc.produce.generate;

import com.github.xlljc.produce.MbLog;
import com.github.xlljc.produce.describe.*;
import com.github.xlljc.produce.table.Table;
import com.github.xlljc.utils.MbCollectionUtil;
import com.github.xlljc.MbMapperException;
import com.github.xlljc.config.MbMapperConfig;
import com.github.xlljc.produce.describe.Class;
import com.github.xlljc.produce.table.Column;
import com.github.xlljc.utils.NameUtil;

import java.util.List;
import java.util.Map;


/**
 * Vo代码生成类
 */
public class VoGenerate {

    private final Table table;
    private final MbMapperConfig config;

    public VoGenerate(MbMapperConfig config, Table table) {
        this.table = table;
        this.config = config;
    }

    public void generate() throws MbMapperException {

        Class cls = new Class();
        //设置对应的表
        cls.setTable(table);
        //设置包名
        cls.setPackageName(config.getVoPackage());
        //设置类名
        cls.setClassName(table.getClassName());
        //设置类注释
        cls.setComment(table.getComment());

        List<Field> fields = cls.getFields();
        //遍历字段
        for (Column column : table.getColumns()) {
            Field field = new Field();
            //设置对应列
            field.setColumn(column);
            //字段名
            field.setName(column.getFieldName());
            //注释
            field.setComment(column.getComment());
            //列类型
            Type type = NameUtil.jdbcType(column.getType());
            if (type == null) {
                throw new MbMapperException(String.format("No Java type matching type '%s' was found, please check your configuration file", column.getTypeName()));
            }
            field.setType(type);
            //引入
            if (type.getImportPackage() != null) {
                cls.addImport(type.getImportPackage());
            }

            fields.add(field);
        }

        //******************** vo相关

        //序列化接口
        List<Type> implementList = cls.getImplementList();
        implementList.add(new Type("Serializable", "java.io.Serializable"));

        //vo类自定义注解
        if (config.getVoClassAnnotation() != null) {
            List<String> list = MbCollectionUtil.parseList(config.getVoClassAnnotation());
            Map<String, Annotation> annotations = cls.getAnnotations();
            for (String s : list) {
                KeyValue<String, String> keyValue = KeyValue.parseKeyValue(s);
                Annotation annotation = new Annotation();
                annotation.setType(new Type(keyValue.getKey(), keyValue.getValue()));
                annotations.put(keyValue.getKey(), annotation);
            }
        }


        MbLog.line();
        MbLog.logInfo(cls.toJavaCode(config));
    }

}
