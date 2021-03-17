package org.mbmapper;


import org.junit.jupiter.api.Test;
import org.mbmapper.config.MbMapperConfig;
import org.mbmapper.config.MbMapperConfigException;
import org.mbmapper.produce.MbLog;
import org.mbmapper.produce.describe.KeyValue;
import org.mbmapper.produce.file.JavaFileLoader;
import org.mbmapper.produce.template.MbMapperTemplateException;
import org.mbmapper.produce.template.Target;
import org.mbmapper.produce.template.target.IfTarget;
import org.mbmapper.utils.FileUtil;
import org.mbmapper.utils.MbCollectionUtil;
import org.mbmapper.utils.RegexUtil;

import java.io.*;
import java.sql.SQLException;
import java.util.List;
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

    //匹配${}
    @Test
    void test2() throws IOException, MbMapperConfigException {
        String s = FileUtil.loadFile(new File("H:\\idea\\mapTest\\template.txt"));
        Matcher matcher = RegexUtil.matcher("(?<!\\\\)\\$\\{([^{}]+?)\\}", s);
        StringBuilder code = new StringBuilder(s);
        int offset = 0;
        while (matcher.find()) {
            String str = matcher.group();
            String str2 = "<<这是替补位置>>";
            int difference = str.length() - str2.length();
            int start = matcher.start(), end = matcher.end();

            code.replace(start - offset, end - offset, str2);
            offset += difference;
        }
        System.out.println(code.toString());
    }

    //匹配成对的标签或者${}
    @Test
    void test3() throws IOException, MbMapperTemplateException {
        String s = FileUtil.loadFile(new File("H:\\idea\\mapTest\\template.txt"));
        Matcher matcher = RegexUtil.matcher("(<#.+>)|((?<!\\\\)\\$\\{.+})", s);

        while (matcher.find()) {
            String str = matcher.group();

            if (str.charAt(0) == '$') {
                System.out.println("${}: " + str);
            } else {
                System.out.println("<>: " + str);
                //获取标签名称
                String name = RegexUtil.findFirst("(?<=<#)[\\w]+", str);
                if (name != null) {
                    Target target = Target.Load(IfTarget.class, str);
                    //System.out.println("name: " + name + ", target: " + target);
                    //记录位置,到时候还要回滚
                    int start = matcher.start();

                    if(name.equalsIgnoreCase("if")) {
                        Matcher matcher1 = RegexUtil.matcher("(<# *if.*>)|(</# *if *>)", s);
                        matcher1.find(start);
                        System.out.println("start: " + start);
                        while (matcher1.find()) {
                            System.out.println("index: " + matcher1.start() + ", g1: " + matcher1.group(1) + ", g2: " + matcher1.group(2));
                        }
                    }

                    //matcher.find(start);
                } else {
                    System.out.println("未取得名称!");
                }

            }
        }
    }

    @Test
    void test4() {
        Matcher matcher = RegexUtil.matcher("(?<open>\\{)[\\w\\W]*(?<-open>\\})", "{{}}{}{}{}{}");
        int index = 0;
        while (matcher.find()) {
            String str = matcher.group();
            System.out.println("str: " + str);
            if (index % 3 == 2) {
                matcher.find();
                matcher.find();
                matcher.find();
                matcher.find(index);
                System.out.println("index: " + index);
            }
            index ++;
        }
    }
}
