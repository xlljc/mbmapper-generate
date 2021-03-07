package org.mbmapper.produce.describe;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class Class {

    /** 包名 */
    private String packageName;
    /** 导入的包 */
    private List<String> imports;
    /** 类型 */
    private DefineType type = DefineType.CLASS;
    /** 类名 */
    private String className;
    /** 字段 */
    private Map<String, Field> fields;
    /** 构造函数 */
    private Map<String, Constructor> constructors;
    /** 方法函数 */
    private Map<String, Method> methods;

}
