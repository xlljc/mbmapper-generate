package org.mbmapper.produce.dao;

import org.mbmapper.produce.table.Column;
import org.mbmapper.produce.table.Table;
import org.mbmapper.utils.DBUtil;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 表结构查询类
 */
public class TableStructDao {

    /**
     * 根据表名获取表结构, 获取表的所有列名以及对应的类型
     * @param tableName 表名
     */
    public Table getTableStruct(String tableName) throws SQLException {
        //table对象
        Table table = new Table();

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
            Column column = new Column();
            //获取数据
            _columnMeta(columns, column);

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

        return table;
    }


    /**
     * 获取列元数据
     * @param columnSet 包含列的 ResultSet 对象
     * @param column 列对象
     */
    private void _columnMeta(ResultSet columnSet, Column column) throws SQLException {
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
        column.setType(columnType);
        column.setTypeName(columnTypeName);
        column.setAutoIncrement(isAutoincrement);
        column.setDefaultVal(defaultVal);
        column.setComment(comment);
        column.setNotNull(notNull);
    }

}
