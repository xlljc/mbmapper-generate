package org.mbmapper.produce.dao;

public class ConnectNotOpenedException extends Exception {

    public ConnectNotOpenedException(String reason) {
        super(reason);
    }

}
