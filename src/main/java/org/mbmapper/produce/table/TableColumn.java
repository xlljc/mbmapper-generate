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
public class TableColumn {
    /**
     * 列名
     */
    private String name;

    /**
     * 值类型
     */
    private String type;

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
     * 列注释
     */
    private String comment;

}
