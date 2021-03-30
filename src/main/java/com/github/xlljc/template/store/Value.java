package com.github.xlljc.template.store;

public class Value {

    /**
     * 当前值的访问权限
     */
    public enum Access {
        /**
         * 允许读写
         */
        writeAndRead,
        /**
         * 只读
         */
        readOnly
    }

    /**
     * 访问权限
     */
    private Access access = Access.writeAndRead;
    /**
     * 值
     */
    private Object value;

    public Value() {
    }

    public Value(Object value) {
        this.value = value;
    }

    public Value(Access access, Object value) {
        this.access = access;
        this.value = value;
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
