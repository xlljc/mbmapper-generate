package com.github.xlljc.produce.generate;

import com.github.xlljc.produce.describe.Class;
import com.github.xlljc.config.MbMapperConfig;

public interface MbHandler {

    void process(MbMapperConfig config, Class cls);

}
