package org.mbmapper.produce.dao;

public class ConnectNotOpenException extends Exception {

    public ConnectNotOpenException(String reason) {
        super(reason);
    }

}
