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
        System.out.println("ConnectDevice.open() => ...");
    }

    /**
     * 关闭连接
     */
    public static void close() {
        if (flag) {
            DBUtil.close(connection, null, null);
            flag = false;
        }
        System.out.println("ConnectDevice.close() => ...");
    }

    /**
     * 执行连接查询操作, 调用 ConnectHandle 中的方法
     */
    public static void connect(ConnectHandle handle) throws SQLException {
        if (!flag)
            try {
                throw new ConnectNotOpenedException("The open() method must be called before the connect() method is called.");
            } catch (ConnectNotOpenedException e) {
                e.printStackTrace();
            }
        else {
            PreparedStatement statement = null;
            ResultSet resultSet = null;
            try {
                statement = handle.prepare(connection);
                if (statement == null)
                    throw new NullPointerException("The ConnectHandle.prepare() method cannot return null.");
                resultSet = statement.executeQuery();
                handle.success(resultSet);
            } finally {
                DBUtil.close(null, statement, resultSet);
            }
        }
    }

}
