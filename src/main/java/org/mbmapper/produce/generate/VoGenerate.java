package org.mbmapper.produce.generate;

import org.mbmapper.config.MbMapperConfig;
import org.mbmapper.produce.table.Table;


/**
 * Vo代码生成类
 */
public class VoGenerate {

    private final Table table;
    private final MbMapperConfig config;

    public VoGenerate(MbMapperConfig config, Table table) {
        this.table = table;
        this.config = config;
    }

}
