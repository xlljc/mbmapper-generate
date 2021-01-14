package org.mbmapper.produce.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 连接数据库执行自定义操作
 */
public interface ConnectHandle {

    PreparedStatement prepare(Connection connection) throws SQLException;

    void success(ResultSet resultSet) throws SQLException;

}
