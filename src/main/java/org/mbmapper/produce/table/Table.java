package org.mbmapper.produce.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 表结构类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Table {

    /**
     * 表名
     */
    private String name;

    /**
     * 主键列
     */
    private String pkColumn;

    /**
     * 自增长列
     */
    private String aiColumn;



    /**
     * 表列数据
     */
    private Map<String, Column> columnMap;

    /**
     * 外键列表
     */
    private Map<String, ForeignKey> foreignKeyMap;


}
