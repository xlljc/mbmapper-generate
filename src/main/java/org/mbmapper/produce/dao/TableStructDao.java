package org.mbmapper.produce.dao;


import org.mbmapper.produce.table.Table;

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
        ConnectDevice.connect(new ConnectHandle() {
            @Override
            public PreparedStatement prepare(Connection connection) throws SQLException {
                return connection.prepareStatement(String.format("select * from %s limit 0", tableName));
            }

            @Override
            public void success(ResultSet resultSet) throws SQLException {
                Map<String, String> map = new HashMap<>();
                ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                int columnCount = resultSetMetaData.getColumnCount();
                for (int i = 1; i <= columnCount; i++) {
                    //if (!autoIncrement) autoIncrement = resultSetMetaData.isAutoIncrement(i);
                    String key = resultSetMetaData.getColumnName(i);
                    String value = resultSetMetaData.getColumnTypeName(i).toLowerCase();
                    map.put(key, value);
                }
                System.out.println(map);
            }
        });
        return null;
    }

}
