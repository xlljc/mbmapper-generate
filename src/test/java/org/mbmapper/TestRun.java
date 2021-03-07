package org.mbmapper;


import org.junit.jupiter.api.Test;
import org.mbmapper.config.MbMapperConfig;
import org.mbmapper.config.MbMapperConfigException;
import org.mbmapper.produce.MbLog;
import org.mbmapper.produce.file.JavaFileLoader;
import org.mbmapper.utils.RegexUtil;

import java.io.*;
import java.sql.SQLException;
import java.util.regex.Matcher;

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
        JavaFileLoader javaFileLoader = new JavaFileLoader("H:\\idea\\mapTest\\vo\\MbMapper.java");
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
        }

    }

    /*@Test
    void test3() {
        String name1 = "AAa_aaa_aaa";
        String name2 = "_Aaa_aaa_aaa";
        String name3 = "a_";
        String name4 = "a";

        System.out.println(toHumpName(name1));
        System.out.println(toHumpName(name2));
        System.out.println(toHumpName(name3));
        System.out.println(toHumpName(name4));
    }

    String toHumpName(String name) {
        Matcher matcher = RegexUtil.matcher("(?<=\\w)_(\\w)", name);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        String s = sb.toString().replace("_","");
        return s.replaceFirst("\\w",s.substring(0,1).toLowerCase());
    }*/
}
