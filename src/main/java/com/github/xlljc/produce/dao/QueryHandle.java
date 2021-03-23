package com.github.xlljc.produce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 连接数据库执行自定义操作
 */
public interface QueryHandle {

    /**
     * 执行查询前的准备工作, 创建sql预编译对象
     * @param connection 连接对象
     */
    PreparedStatement prepare(Connection connection) throws SQLException;

    /**
     * 查询成功返回结果
     * @param resultSet 查询的结果集
     */
    void success(ResultSet resultSet) throws SQLException;

}
