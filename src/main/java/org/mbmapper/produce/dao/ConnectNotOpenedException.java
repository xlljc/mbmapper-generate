package org.mbmapper.produce.dao;

/**
 * 连接未打卡异常
 */
public class ConnectNotOpenedException extends Exception {

    public ConnectNotOpenedException(String reason) {
        super(reason);
    }

}
