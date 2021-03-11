package org.mbmapper;


import org.junit.jupiter.api.Test;
import org.mbmapper.config.MbMapperConfig;
import org.mbmapper.config.MbMapperConfigException;
import org.mbmapper.produce.describe.KeyValue;
import org.mbmapper.utils.MbCollectionUtil;

import java.io.*;
import java.sql.SQLException;
import java.util.List;

public class TestRun {

    @Test
    void test1() throws IOException, ClassNotFoundException, MbMapperConfigException, SQLException {

        MbMapperConfig config = new MbMapperConfig();
        MbMapper mbMapper = new MbMapper(config);
        mbMapper.init();
        //mbMapper.generate();
        try {
            mbMapper.generateVo();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            mbMapper.close();
        }
    }

    @Test
    void test2() throws IOException {
        //MbMapperConfig config = new MbMapperConfig();
        /*JavaFileLoader javaFileLoader = new JavaFileLoader("H:\\idea\\mapTest\\vo\\MbMapper.java");
        javaFileLoader.load();
        String javaContent = javaFileLoader.getJavaContent();
        System.out.println(javaContent);
        MbLog.line();

        Matcher matcher =
                RegexUtil.matcher("[{}]", javaContent);

        int layer = 1,count = 0;
        int start = 0,end = 0;
        while (matcher.find()) {
            String s = matcher.group();
            if ("{".equals(s)) {
                count++;
                if (layer == count) {
                    start = matcher.start();
                }
            } else if ("}".equals(s)) {
                count--;
                if (layer == count) {
                    //MbLog.line();
                    System.out.println(javaContent.substring(start,matcher.end()));
                }
            }
        }*/

    }

    @Test
    void test3() throws MbMapperConfigException {
        MbMapperConfig config = new MbMapperConfig();
        System.out.println(config.getVoClassAnnotation());
        List<String> strings = MbCollectionUtil.parseList(config.getVoClassAnnotation());
        strings.forEach(item -> {
            System.out.println(KeyValue.parseKeyValue(item));
        });
        //System.out.println();
    }

}
