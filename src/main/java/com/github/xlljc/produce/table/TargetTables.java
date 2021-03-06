package com.github.xlljc.produce.table;

import com.github.xlljc.produce.MbLog;
import lombok.Data;
import com.github.xlljc.config.MbMapperConfig;
import com.github.xlljc.config.MbMapperConfigException;
import com.github.xlljc.produce.dao.TableStructDao;
import com.github.xlljc.utils.MbCollectionUtil;
import com.github.xlljc.utils.RegexUtil;

import java.sql.SQLException;
import java.util.*;

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
    private List<Table> tables;


    private final TableStructDao structDao;

    /**
     * 初始化, 加载所有表名
     * @param config 配置文件
     */
    public TargetTables(MbMapperConfig config) throws SQLException {
        this.config = config;
        structDao = new TableStructDao(config);

        //读取配置
        String tablesStr = config.getTables();

        if (RegexUtil.matches("^\\s*\\*\\s*$", tablesStr)) {    //是否为全部的表
            MbLog.logInfo("TargetTables.TargetTables(): => All of the table.");
            //获取所有的表
            tableNames = structDao.getAllTableNames();

        } else if (RegexUtil.matches("^\\s*!\\s*\\[[\\s\\S]*]$", tablesStr)) {  //不包含的表
            tablesStr = tablesStr.replaceAll("(^\\s*!)|(\\[\\s*,*)|(,*\\s*])|(\\s)", "");
            MbLog.logInfo(String.format("TargetTables.TargetTables(): => Table not included [%s].", tablesStr));
            //获取所有的表
            tableNames = structDao.getAllTableNames();
            //分割表
            List<String> notTables = Arrays.asList(tablesStr.split("(?<!\\\\),"));
            //移除包含的表
            tableNames.removeAll(notTables);

        } else if (RegexUtil.matches("^\\s*\\[[\\s\\S]*]$", tablesStr)) {   //指定的表
            tablesStr = tablesStr.replaceAll("(\\[\\s*,*)|(,*\\s*])|(\\s)", "");
            MbLog.logInfo(String.format("TargetTables.TargetTables(): => Table included [%s].", tablesStr));

            //分割表
            tableNames = new ArrayList<>(Arrays.asList(tablesStr.split("(?<!\\\\),")));
        } else {    //不认识的配置项
            new MbMapperConfigException(String.format("Cannot load configuration 'tables': '%s',please check your syntax.", tablesStr))
                    .printStackTrace();
            tableNames = new ArrayList<>();
        }
        MbLog.logInfo(String.format("TargetTables.TargetTables(): => %s.", tableNames));
    }


    /**
     * 加载类的表的构造, 之后就可以获取 tables 了
     */
    public void load() throws SQLException {
        tables = new ArrayList<>();
        //tableNames.
        //先来基础的, 直接遍历
        int length = tableNames.size();
        for (int i = 0; i < length; i++) {
            String name = tableNames.get(i);
            //获取表结构, 如果 tableNames 被修改, 那么索引会变化
            Table table = structDao.getTableStruct(name, tableNames);
            //如果长度发生过变化, //**就将索引向前移动
            if (length != tableNames.size()) {
                length = tableNames.size();
                //**i--;
            }
            tables.add(table);
        }
        //打印日志
        _loadLog();
    }

    /**
     * 输出读取日志, 打印所有表
     */
    private void _loadLog() {
        // 日志打印
        MbLog.line();
        MbLog.logSuccess("TargetTables.load(): => The table was loaded successfully, printed as follows:");
        //遍历表
        for (Table table : tables) {
            //日志字符串
            StringBuilder logStr = new StringBuilder(
                    String.format("              table: => tableName: %s%n                        column: ", table.getName()));

            //遍历列
            for (Column column : table.getColumns()) {
                String str = column.getName();
                List<String> temp = new ArrayList<>();
                //如果该列存在外键
                if (table.getForeignKeyMap().containsKey(column.getName()))
                    temp.add("fk");
                //如果该列是主键
                if (column.isPrimaryKey())
                    temp.add("pk");
                //如果该列是自增长列
                if (column.isAutoIncrement())
                    temp.add("incr");

                //拼接字符串
                if (temp.size() > 0)
                    str += MbCollectionUtil.join(temp, ",", "(", ")");
                logStr.append(str).append("  ");
            }
            MbLog.log(logStr.toString());
        }
    }


}
