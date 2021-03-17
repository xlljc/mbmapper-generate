package org.mbmapper.produce.generate;

import org.mbmapper.MbMapperException;
import org.mbmapper.config.MbMapperConfig;
import org.mbmapper.produce.table.Table;

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