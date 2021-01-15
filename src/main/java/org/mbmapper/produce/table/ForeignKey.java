package org.mbmapper.produce.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 外键列表
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForeignKey {

    /**
     * 当前表名
     */
    private String thisTable;

    /**
     * 外键指向表名
     */
    private String otherTable;

    /**
     * 外键列
     */
    private String fkColumn;

    /**
     * 主键列
     */
    private String pkColumn;




}
