package com.github.xlljc.produce.dao;

/**
 * 连接未打开异常
 */
public class ConnectNotOpenedException extends Exception {

    public ConnectNotOpenedException(String reason) {
        super(reason);
    }

}
