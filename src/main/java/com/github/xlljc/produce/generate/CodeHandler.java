package com.github.xlljc.produce.generate;

import com.github.xlljc.produce.table.Table;
import com.github.xlljc.MbMapperException;
import com.github.xlljc.config.MbMapperConfig;

import java.util.List;

/**
 * 代码处理对象
 */
public class CodeHandler {

    private final List<Table> tables;
    private final MbMapperConfig config;

    public CodeHandler(MbMapperConfig config, List<Table> tables) {
        this.tables = tables;
        this.config = config;
    }

    public void dispose() throws MbMapperException {
        for (Table table : tables) {
            new VoGenerate(config,table).generate();
        }
    }


}