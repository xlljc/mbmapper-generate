package org.mbmapper;


import org.junit.jupiter.api.Test;
import org.mbmapper.config.MbMapperConfig;

import java.io.*;

public class TestRun {

    @Test
    void test1() throws IOException, ClassNotFoundException {
        MbMapperConfig config = new MbMapperConfig("mbmapper.properties");
        System.out.println(config);
        MbMapper mbMapper = new MbMapper(config);
        mbMapper.generate();

    }

}
