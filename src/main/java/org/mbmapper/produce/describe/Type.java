package org.mbmapper.produce.describe;

/**
 * 类型描述
 */
public class Type {

    private String name;
    private String importPackage;

    public Type() {
    }

    public Type(String name, String importPackage) {
        this.name = name;
        this.importPackage = importPackage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImportPackage() {
        return importPackage;
    }

    public void setImportPackage(String importPackage) {
        this.importPackage = importPackage;
    }
}
