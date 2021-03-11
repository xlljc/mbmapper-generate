package org.mbmapper.produce.generate;

import org.mbmapper.MbMapperException;
import org.mbmapper.config.MbMapperConfig;
import org.mbmapper.produce.MbLog;
import org.mbmapper.produce.describe.Class;
import org.mbmapper.produce.describe.Field;
import org.mbmapper.produce.describe.Type;
import org.mbmapper.produce.table.Column;
import org.mbmapper.produce.table.Table;
import org.mbmapper.utils.NameUtil;

import java.util.List;

/**
 * 代码生成器类
 */
public class Generate {

    private final List<Table> tables;
    private final MbMapperConfig config;

    public Generate(MbMapperConfig config, List<Table> tables) {
        this.tables = tables;
        this.config = config;
    }

    public void generate() throws MbMapperException {



        for (Table table : tables) {



            Class cls = new Class();
            //设置对应的表
            cls.setTable(table);
            //设置包名
            cls.setPackageName(config.getVoPackage());
            //设置类名
            cls.setClassName(table.getClassName());
            //设置类注释
            cls.setComment(table.getComment());

            //序列化接口
            List<Type> implementList = cls.getImplementList();
            implementList.add(new Type("Serializable", "java.io.Serializable"));

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

            MbLog.line();
            MbLog.logInfo(cls.toJavaCode(config));
        }
    }


}