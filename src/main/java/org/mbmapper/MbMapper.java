package org.mbmapper;

import org.mbmapper.config.MbMapperConfig;
import org.mbmapper.produce.MbLog;
import org.mbmapper.produce.dao.ConnectDevice;
import org.mbmapper.produce.describe.Class;
import org.mbmapper.produce.table.Table;
import org.mbmapper.produce.table.TargetTables;
import org.mbmapper.utils.DBUtil;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;
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


            tables.keySet().forEach(key -> {
                Table table = tables.get(key);

                Class cls = new Class();

                //设置包名
                cls.setPackageName(config.getVoPackage());
                //设置类名
                cls.setClassName(table.getClassName());
                //设置类注解
                cls.setComment(table.getComment());


                MbLog.logSuccess("cls: => " + cls);
                MbLog.line();
                MbLog.logInfo(cls.toJavaCode());
            });


        } catch (SQLException e) {
            MbLog.logError("MbMapper.generateVo() has exception: => " + e.getMessage());
            //e.printStackTrace();
            throw e;
        } catch (Exception e) {
            MbLog.logError("MbMapper.generateVo() has exception: => " + e.getMessage());
        }
    }



}
