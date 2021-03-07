package org.mbmapper.produce.dao;

import org.mbmapper.config.MbMapperConfig;
import org.mbmapper.produce.table.Column;
import org.mbmapper.produce.table.ForeignKey;
import org.mbmapper.produce.table.Table;
import org.mbmapper.utils.DBUtil;
import org.mbmapper.utils.NameUtil;
import org.mbmapper.utils.RegexUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 表结构查询类
 */
public class TableStructDao {

    /**
     * 配置文件
     */
    private final MbMapperConfig config;

    /**
     * 代配置初始化
     */
    public TableStructDao(MbMapperConfig config) {
        this.config = config;
    }

    /**
     * 获取数据库中所有的表名称
     *
     * @return 表名称集合
     */
    public List<String> getAllTableNames() throws SQLException {
        List<String> tableNames = new ArrayList<>();

        Connection connection = ConnectDevice.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        //获取所有表, 遍历
        ResultSet tables = metaData.getTables(connection.getCatalog(), null, null, null);
        while (tables.next()) {
            tableNames.add(tables.getString("TABLE_NAME"));
        }
        return tableNames;
    }

    /**
     * 根据表名获取表结构, 获取表的所有列名以及对应的类型
     *
     * @param tableName  表名
     * @param tableNames 所有的表名
     */
    public Table getTableStruct(String tableName, final List<String> tableNames) throws SQLException {
        //table对象
        Table table = new Table();
        table.setName(tableName);
        table.setClassName(NameUtil.firstToUpperCase(NameUtil.toHumpName(tableName)));

        //创建表的列结构, map键值对
        Map<String, Column> columnMap = new HashMap<>();
        table.setColumnMap(columnMap);

        //获取到
        Connection connection = ConnectDevice.getConnection();
        DatabaseMetaData metaData = connection.getMetaData();
        assert metaData != null;

        //通过元数据获取表的所有列
        ResultSet columns = metaData.getColumns(connection.getCatalog(), null, tableName, null);
        while (columns.next()) {
            //获取数据
            Column column = _columnMeta(columns);
            //根据列名称加入到 columnMap 集合中
            columnMap.put(column.getName(), column);

            //判断自增长列
            if (table.getAiColumn() == null && column.isAutoIncrement()) {
                table.setAiColumn(column.getName());
            }
        }
        DBUtil.close(null, null, columns);

        //获取主键列
        ResultSet primaryKey = metaData.getPrimaryKeys(connection.getCatalog(), null, tableName);
        if (primaryKey.next()) {
            //主键列名
            String primaryKeyName = primaryKey.getString("COLUMN_NAME");

            table.setPkColumn(primaryKeyName);
            table.getColumnMap().get(primaryKeyName).setPrimaryKey(true);
        }
        DBUtil.close(null, null, primaryKey);

        //获取唯一标识列
        ResultSet uniqueKey = metaData.getIndexInfo(connection.getCatalog(), null, tableName, true, false);
        while (uniqueKey.next()) {
            String unique = uniqueKey.getString("COLUMN_NAME");
            table.getColumnMap().get(unique).setUnique(true);
        }
        DBUtil.close(null, null, uniqueKey);

        //获取表注释
        //metaData.getTables()... getString("REMARKS") 无效 ??? , 拿不到表注释 ???
        //用这种方式
        ConnectDevice.query(new QueryHandle() {
            @Override
            public PreparedStatement prepare(Connection connection) throws SQLException {
                return connection.prepareStatement(String.format(
                        "SELECT table_comment FROM information_schema.TABLES WHERE table_schema = '%s' and TABLE_NAME = '%s' ORDER BY table_name;",
                        connection.getCatalog(), tableName));
            }

            @Override
            public void success(ResultSet resultSet) throws SQLException {
                if (resultSet.next()) {
                    String comment = resultSet.getString(1);
                    if (comment != null && comment.isEmpty()) comment = null;
                    table.setComment(comment);
                }
            }
        });

        //获取外键列表
        Map<String, ForeignKey> foreignKeyMap = new HashMap<>();
        table.setForeignKeyMap(foreignKeyMap);

        ResultSet importedKeys = metaData.getImportedKeys(connection.getCatalog(), null, tableName);
        if (config.isUseRope()) {   //开启了捆绑关联
            while (importedKeys.next()) {
                ForeignKey foreignKey = _foreignKeyMeta(importedKeys, tableName);
                //如果外键表不在写出列队中, 就将外键表写入到列队中
                if (!tableNames.contains(foreignKey.getOtherTable())) {
                    tableNames.add(foreignKey.getOtherTable());
                }
                foreignKeyMap.put(foreignKey.getFkColumn(), foreignKey);
            }
        } else {    //未开启捆绑关联
            while (importedKeys.next()) {
                ForeignKey foreignKey = _foreignKeyMeta(importedKeys, tableName);
                //如果外键表不在写出列队中, 当前表就不显该示外键
                if (tableNames.contains(foreignKey.getOtherTable())) {
                    foreignKeyMap.put(foreignKey.getFkColumn(), foreignKey);
                }
            }
        }
        DBUtil.close(null, null, importedKeys);

        return table;
    }


    /**
     * 获取列元数据
     *
     * @param columnSet 包含列的 ResultSet 对象
     */
    private Column _columnMeta(ResultSet columnSet) throws SQLException {
        Column column = new Column();
        //获取列名
        String columnName = columnSet.getString("COLUMN_NAME");
        //获取类型
        int columnType = columnSet.getInt("DATA_TYPE");
        //获取类型名称
        String columnTypeName = columnSet.getString("TYPE_NAME");
        //获取是否自增长
        boolean isAutoincrement = "YES".equals(columnSet.getString("IS_AUTOINCREMENT"));
        //获取是否有默认值
        String defaultVal = columnSet.getString("COLUMN_DEF");
        //获取列注释
        String comment = columnSet.getString("REMARKS");
        if (comment != null && comment.isEmpty()) comment = null;
        //获取是否非空
        boolean notNull = !"YES".equals(columnSet.getString("IS_NULLABLE"));

        column.setName(columnName);
        column.setFieldName(NameUtil.firstToLowerCase(NameUtil.toHumpName(columnName)));
        column.setType(columnType);
        column.setTypeName(columnTypeName);
        column.setAutoIncrement(isAutoincrement);
        column.setDefaultVal(defaultVal);
        column.setComment(comment);
        column.setNotNull(notNull);
        return column;
    }

    /**
     * 获取外键元数据
     *
     * @param importedKeys 包含外键的 ResultSet 集合
     * @param tableName    当前表名
     */
    private ForeignKey _foreignKeyMeta(ResultSet importedKeys, String tableName) throws SQLException {
        //外键列名
        String fkColumnName = importedKeys.getString("FKCOLUMN_NAME");
        //主键表名
        String pkTableName = importedKeys.getString("PKTABLE_NAME");
        //主键列名
        String pkColumnName = importedKeys.getString("PKCOLUMN_NAME");
        return new ForeignKey(tableName, pkTableName, fkColumnName, pkColumnName);
    }
}
