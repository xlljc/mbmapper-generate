package org.mbmapper.produce.describe;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Annotation {

    /**
     * 类型
     */
    private Type type;

    /**
     * 参数列表
     */
    private List<KeyValue<String,String>> params = new ArrayList<>();

}
