package org.mbmapper.produce.table;

import lombok.Data;
import org.mbmapper.config.MbMapperConfig;
import org.mbmapper.produce.dao.MbMapperConfigException;
import org.mbmapper.produce.dao.TableStructDao;
import org.mbmapper.utils.RegexUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 需要写出的tables
 */
@Data
public class TargetTables {

    /** 配置文件 */
    private final MbMapperConfig config;

    /**  所有需要写出的表名 */
    private List<String> tableNames;

    /** 所有需要写出的表对象 */
    private List<Column> tables;

    /**
     * 初始化, 加载所有表
     * @param config 配置文件
     */
    public TargetTables(MbMapperConfig config) throws SQLException {
        this.config = config;

        TableStructDao structDao = new TableStructDao(config);

        //读取配置
        String tablesStr = config.getTables();

        if (RegexUtil.matches("^\\s*\\*\\s*$", tablesStr)) {                  //是否为全部的表
            System.out.println("TargetTables.TargetTables(): => All of the table");
            //获取所有的表
            tableNames = structDao.getAllTableNames();

        } else if (RegexUtil.matches("^\\s*!\\s*\\[[\\s\\S]*]$", tablesStr)) {           //不包含的表
            //"".replaceAll("^\\s*!\\s*\\[[\\s\\S]*]$","");
            tablesStr = tablesStr.replaceAll("(^\\s*!)|(\\[\\s*,*)|(,*\\s*])|(\\s)", "");
            System.out.printf("TargetTables.TargetTables(): => Table not included [%s]%n", tablesStr);
            //获取所有的表
            tableNames = structDao.getAllTableNames();
            //分割表
            List<String> notTables = Arrays.asList(tablesStr.split(","));
            //移除包含的表
            tableNames.removeAll(notTables);

        } else if (RegexUtil.matches("^\\s*\\[[\\s\\S]*]$", tablesStr)) {                                                                    //包含的表
            tablesStr = tablesStr.replaceAll("(\\[\\s*,*)|(,*\\s*])|(\\s)", "");
            System.out.printf("TargetTables.TargetTables(): => Table included [%s]%n", tablesStr);

            //分割表
            tableNames = Arrays.asList(tablesStr.split(","));
        } else {
            new MbMapperConfigException(String.format("Cannot load configuration 'tables': '%s',please check your syntax.", tablesStr))
                    .printStackTrace();
            tableNames = new ArrayList<>();
        }
        System.out.printf("TargetTables.TargetTables(): => %s%n", tableNames);

    }




}
