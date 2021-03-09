package org.mbmapper;

import org.mbmapper.config.MbMapperConfig;
import org.mbmapper.produce.MbLog;
import org.mbmapper.produce.dao.ConnectDevice;
import org.mbmapper.produce.describe.Class;
import org.mbmapper.produce.describe.Constructor;
import org.mbmapper.produce.describe.Field;
import org.mbmapper.produce.describe.KeyValue;
import org.mbmapper.produce.table.Column;
import org.mbmapper.produce.table.Table;
import org.mbmapper.produce.table.TargetTables;
import org.mbmapper.utils.DBUtil;
import org.mbmapper.utils.NameUtil;

import java.sql.SQLException;
import java.util.*;
import java.util.function.Consumer;


public class MbMapper {

    /** 传入的配置文件 */
    private final MbMapperConfig config;

    public MbMapper(MbMapperConfig config) throws ClassNotFoundException {
        this.config = config;
        //初始化数据库配置
        DBUtil.init(config.getUrl(),config.getUser(),config.getPassword(),config.getDriver());
    }

    /**
     * 初始化调用, 在创建实例后第一个调用的方法应该是此方法, 用于初始化表数据
     */
    public void init() throws SQLException {
        // 打开日志
        MbLog.start(config);
        // 打开数据库连接
        ConnectDevice.open();
    }

    /**
     * 在执行完写出操作后, 最后不要忘了调用该方法释放资源
     */
    public void close() throws SQLException {
        // 关闭数据库连接
        ConnectDevice.close();
        // 执行写出操作
        MbLog.write();
    }

    /**
     * 执行生成操作, 会生成所有的代码, 包括vo层, dao层, 以及xml代码
     */
    public void generate() {
        // dao层接口存放路径
        String daoDir = config.getJavaDir() + "/" + config.getDaoPackage().replaceAll("\\.","/");
        // vo存放路径
        String voDir = config.getJavaDir() + "/" + config.getVoPackage().replaceAll("\\.","/");

        System.out.println(daoDir);
        System.out.println(voDir);

    }


    /**
     * 执行生成操作, 只生成Vo代码
     */
    public void generateVo() throws SQLException {
        try {
            TargetTables targetTables = new TargetTables(config);
            targetTables.load();
            Map<String, Table> tables = targetTables.getTables();

            //************** 创建java code ****************

            tables.keySet().forEach(key -> {
                Table table = tables.get(key);

                Class cls = new Class();

                //设置包名
                cls.setPackageName(config.getVoPackage());
                //设置类名
                cls.setClassName(table.getClassName());
                //设置类注释
                cls.setComment(table.getComment());

                //构造函数(无参构造)

                Map<String, Field> fields = new HashMap<>();
                cls.setFields(fields);
                //遍历字段
                table.getColumnMap().keySet().forEach(columnName -> {
                    Column column = table.getColumnMap().get(columnName);
                    Field field = new Field();
                    //字段名
                    field.setName(column.getFieldName());
                    //列名
                    field.setColumnName(column.getName());
                    //注释
                    field.setComment(column.getComment());
                    //列类型
                    KeyValue<String, String> typeKV = NameUtil.jdbcType(column.getType());
                    if (typeKV == null) {
                        //throw new MbMapperException(String.format("No Java type matching type '%s' was found, please check your configuration file", column.getTypeName()));
                    }
                    field.setType(typeKV.getKey());
                    if (typeKV.getValue() != null) {
                        cls.addImport(typeKV.getValue());
                    }

                    fields.put(column.getFieldName(),field);
                });


                MbLog.logSuccess("cls: => " + cls);
                MbLog.line();
                MbLog.logInfo(cls.toJavaCode());
            });

            //***********************************************

        } catch (SQLException e) {
            MbLog.logError("MbMapper.generateVo() has exception: => " + e.getMessage());
            //e.printStackTrace();
            throw e;
        } catch (Exception e) {
            MbLog.logError("MbMapper.generateVo() has exception: => " + e.getMessage());
        }
    }



}
