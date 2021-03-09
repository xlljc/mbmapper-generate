package org.mbmapper.produce.describe;

public enum AccessModify {

    PUBLIC("public "),
    DEFAULT(""),
    PROTECTED("protected"),
    PRIVATE("private ");

    private final String value;

    AccessModify(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
