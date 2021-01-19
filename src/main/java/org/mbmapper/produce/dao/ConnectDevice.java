package org.mbmapper.produce.dao;


import java.sql.*;

import org.mbmapper.produce.MbLog;
import org.mbmapper.utils.DBUtil;

public class ConnectDevice {

    /**
     * 是否已经开启连接了
     */
    private static boolean flag = false;

    /**
     * 数据库连接对象
     */
    private static Connection connection;


    /**
     * 打开连接
     */
    public static void open() {
        if (!flag) {
            connection = DBUtil.getConn();
            flag = true;
        }
        MbLog.log("ConnectDevice.close(): => Open connection successfully.");
    }

    /**
     * 关闭连接
     */
    public static void close() {
        if (flag) {
            DBUtil.close(connection, null, null);
            flag = false;
        }
        MbLog.log("ConnectDevice.close(): => The connection is broken.");
    }

    /**
     * 获取连接对象, 请勿手动关闭连接对象
     */
    public static Connection getConnection() {
        //如果没打卡连接,抛出异常
        if (!flag)
            try {
                throw new ConnectNotOpenedException("The open() method must be called before the connect() method is called.");
            } catch (ConnectNotOpenedException e) {
                e.printStackTrace();
            }
        return connection;
    }

    /**
     * 获取数据库元数据
     */
    public static DatabaseMetaData getMetaData() throws SQLException {
        Connection connection = getConnection();
        return connection != null ? connection.getMetaData() : null;
    }

    /**
     * 执行连接查询操作, 委托 QueryHandle
     */
    public static void query(QueryHandle handle) throws SQLException {
        Connection connection = getConnection();
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            if ((statement = handle.prepare(connection)) == null)
                throw new NullPointerException("The ConnectHandle.prepare() method cannot return null.");
            handle.success(resultSet = statement.executeQuery());
        } finally {
            DBUtil.close(null, statement, resultSet);
        }
    }

}
