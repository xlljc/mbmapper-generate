package com.github.xlljc.produce.describe;

import java.util.HashMap;
import java.util.Map;

/**
 * 参数描述
 */
public class Param {

    /**
     * 参数类型
     */
    private Type type;
    /**
     * 参数名称
     */
    private String name;
    /**
     * 注解
     */
    private Map<String,Annotation> annotationMap = new HashMap<>();

    public Param(){
    }

    public Param(Type type, String name, Map<String, Annotation> annotationMap) {
        this.type = type;
        this.name = name;
        this.annotationMap = annotationMap;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Annotation> getAnnotationMap() {
        return annotationMap;
    }

    public void setAnnotationMap(Map<String, Annotation> annotationMap) {
        this.annotationMap = annotationMap;
    }
}
