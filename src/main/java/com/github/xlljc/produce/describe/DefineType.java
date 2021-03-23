package com.github.xlljc.produce.describe;

/**
 * 定义的类型
 */
public enum DefineType {

    CLASS("class"),
    ABSTRACT("abstract class"),
    INTERFACE("interface");

    private final String value;
    DefineType(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
