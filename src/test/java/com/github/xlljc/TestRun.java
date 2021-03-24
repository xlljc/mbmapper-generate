package com.github.xlljc;


import com.github.xlljc.config.MbMapperConfig;
import com.github.xlljc.config.MbMapperConfigException;
import com.github.xlljc.template.MbMapperTemplateException;
import com.github.xlljc.template.Template;
import com.github.xlljc.template.guide.TargetGuide;
import com.github.xlljc.template.guide.TargetRegistry;
import com.github.xlljc.utils.FileUtil;
import com.github.xlljc.utils.RegexUtil;
import org.junit.jupiter.api.Test;

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
    void test3() throws Exception {
        String s = FileUtil.loadFile(new File("H:\\idea\\mapTest\\template.ctp"));
        //Matcher matcher = RegexUtil.matcher("(<#.+>)|((?<!\\\\)\\$\\{.+})", s);

        System.out.println("----------------------------- 源代码: \n" + s);
        Template template = new Template(s, TargetGuide.initGuide(new TargetRegistry()));
        String code = template.conversion();
        System.out.println("----------------------------- 最终代码: -----------------------------\n" + code);
    }


    @Test
    void test4() {

    }
}
