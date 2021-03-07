package org.mbmapper.produce.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 表的列
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Column {
    /**
     * 列名
     */
    private String name;

    /**
     * 对应的字段名
     */
    private String fieldName;

    /**
     * 值类型
     */
    private int type;

    /**
     * 值类型名称
     */
    private String typeName;

    /**
     * 默认值
     */
    private String defaultVal;

    /**
     * 是否自增长
     */
    private boolean autoIncrement;

    /**
     * 是否是主键
     */
    private boolean primaryKey;

    /**
     * 是否非空
     */
    private boolean notNull;

    /**
     * 是否是唯一键
     */
    private boolean unique;

    /**
     * 列注释
     */
    private String comment;

}
