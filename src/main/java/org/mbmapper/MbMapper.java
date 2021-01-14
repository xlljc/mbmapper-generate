package org.mbmapper;

import org.mbmapper.config.MbMapperConfig;
import org.mbmapper.produce.dao.ConnectDevice;
import org.mbmapper.produce.dao.ConnectHandle;
import org.mbmapper.produce.dao.ConnectNotOpenException;
import org.mbmapper.utils.DBUtil;

import java.io.File;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class MbMapper {

    private final MbMapperConfig config;

    public MbMapper(MbMapperConfig config) throws ClassNotFoundException {
        this.config = config;
        //初始化数据库配置
        DBUtil.init(config.getUrl(),config.getUser(),config.getPassword(),config.getDriver());
    }

    /**
     * 执行生成操作, 会生成所有的代码, 包括vo层, dao层, 以及xml代码
     */
    public void generate() {
        // dao层接口存放路径
        String daoDir = config.getSrcDir() + "/" + config.getDaoPackage().replaceAll("\\.","/");
        // vo存放路径
        String voDir = config.getSrcDir() + "/" + config.getVoPackage().replaceAll("\\.","/");

        System.out.println(daoDir);
        System.out.println(voDir);

    }


    /**
     * 执行生成操作, 只生成Vo代码
     */
    public void generateVo() {
        //获取表的所有列名以及对应的类型
        String table = "user";
        /*Connection conn = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            Map<String, String> map = new HashMap<>();
            conn = DBUtil.getConn();
            statement = conn.prepareStatement(String.format("select * from %s limit 0", table));
            resultSet = statement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                //if (!autoIncrement) autoIncrement = resultSetMetaData.isAutoIncrement(i + 1);
                String key = resultSetMetaData.getColumnName(i);
                String value = resultSetMetaData.getColumnTypeName(i).toLowerCase();
                map.put(key, value);
            }
            System.out.println(map);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            DBUtil.close(conn,statement,resultSet);
        }*/
        ConnectDevice.open();
        try {
            ConnectDevice.connect(new ConnectHandle() {
                @Override
                public PreparedStatement prepare(Connection connection) throws SQLException {
                    return connection.prepareStatement(String.format("select * from %s limit 0", table));
                }
                @Override
                public void success(ResultSet resultSet) throws SQLException {
                    Map<String, String> map = new HashMap<>();
                    ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
                    int columnCount = resultSetMetaData.getColumnCount();
                    for (int i = 1; i <= columnCount; i++) {
                        //if (!autoIncrement) autoIncrement = resultSetMetaData.isAutoIncrement(i + 1);
                        String key = resultSetMetaData.getColumnName(i);
                        String value = resultSetMetaData.getColumnTypeName(i).toLowerCase();
                        map.put(key, value);
                    }
                    System.out.println(map);
                }
            });
        } catch (SQLException | ConnectNotOpenException throwables) {
            throwables.printStackTrace();
        }
        ConnectDevice.close();
    }



}
