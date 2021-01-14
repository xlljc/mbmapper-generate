package org.mbmapper.produce.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.mbmapper.utils.DBUtil;

public class ConnectDevice {

    /**
     * 是否已经开启连接了
     */
    private static boolean flag = false;

    private static Connection connection;


    /**
     * 打开连接
     */
    public static void open() {
        if (!flag) {
            connection = DBUtil.getConn();
            flag = true;
        }
    }

    /**
     * 关闭连接
     */
    public static void close() {
        if (flag) {
            DBUtil.close(connection, null, null);
            flag = false;
        }
    }

    /**
     *
     */
    public static void connect(ConnectHandle handle) throws SQLException, ConnectNotOpenException {
        if (!flag) throw new ConnectNotOpenException("The open() method must be called before the connect() method is called!");
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            statement = handle.prepare(connection);
            resultSet = statement.executeQuery();
            handle.success(resultSet);
        } finally {
            DBUtil.close(null, statement, resultSet);
        }
    }

}
