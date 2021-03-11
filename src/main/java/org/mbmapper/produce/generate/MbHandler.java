package org.mbmapper.produce.generate;

import org.mbmapper.config.MbMapperConfig;
import org.mbmapper.produce.describe.Class;

public interface MbHandler {

    void process(MbMapperConfig config, Class cls);

}
