package org.mbmapper;

import org.mbmapper.config.MbMapperConfig;
import org.mbmapper.produce.MbLog;
import org.mbmapper.produce.dao.ConnectDevice;
import org.mbmapper.produce.table.Table;
import org.mbmapper.produce.table.TargetTables;
import org.mbmapper.utils.DBUtil;

import java.sql.SQLException;
import java.util.Map;
import java.util.Set;


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
    public void init() {
        // 打开数据库连接
        ConnectDevice.open();
        // 打开日志
        MbLog.start(config);
    }

    /**
     * 在执行完写出操作后, 最后不要忘了调用该方法释放资源
     */
    public void close() {
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
        String daoDir = config.getSrcDir() + "/" + config.getDaoPackage().replaceAll("\\.","/");
        // vo存放路径
        String voDir = config.getSrcDir() + "/" + config.getVoPackage().replaceAll("\\.","/");

        System.out.println(daoDir);
        System.out.println(voDir);

    }


    /**
     * 执行生成操作, 只生成Vo代码
     */
    public void generateVo() throws SQLException {
        //TableStructDao tableStructDao = new TableStructDao(config);
        //Table table = tableStructDao.getTableStruct("user");
        //System.out.println(table);

        TargetTables targetTables = new TargetTables(config);
        targetTables.load();
        Map<String, Table> tables = targetTables.getTables();


    }



}
