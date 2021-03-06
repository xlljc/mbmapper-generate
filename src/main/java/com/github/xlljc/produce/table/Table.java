package com.github.xlljc.produce.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
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
     * 对应的类名
     */
    private String className;

    /**
     * 表注释
     */
    private String comment;

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
    private List<Column> columns;

    /**
     * 外键列表
     */
    private Map<String, ForeignKey> foreignKeyMap = new HashMap<>();


}
