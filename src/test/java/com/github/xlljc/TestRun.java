package com.github.xlljc;


import com.github.xlljc.config.MbMapperConfig;
import com.github.xlljc.config.MbMapperConfigException;
import com.github.xlljc.template.Template;
import com.github.xlljc.template.interfaces.Guide;
import com.github.xlljc.template.store.*;
import com.github.xlljc.template.target.IfTarget;
import com.github.xlljc.template.target.Target;
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


    @Test
    void test3() throws Exception {
        String s = FileUtil.loadFile(new File("H:\\idea\\mapTest\\template.ctp"));
        Guide<Class<? extends Target>> guide = new TargetGuide();
        guide.registry(new TargetRegistry());
        Template template = new Template(s, guide);

        System.out.println("----------------------------- 源代码: \n" + s);

        String code = template.conversion();
        System.out.println("----------------------------- 最终代码: -----------------------------\n" + code);
    }


    /**
     * 测试 DataStore
     */
    @Test
    void test4() {
        DataStore store = new DataStore();
        store.lock();
        store.declareVar("com.xl.aaa", "aaa");
        store.declareReadonlyVar("com.xl.bbb", "bbb");
        store.setVal("com.xl.aaa", "3a");
        store.setVal("com.xl.bbb.value", new char[]{'3', 'b'});

        System.out.println(store.getVal("com.xl.aaa"));
        System.out.println(store.getVal("com.xl.bbb"));

        store.printData();
    }

    @Test
    void test5() {
        StoreRouteNode root = new StoreRouteNode();
        Value value = new Value();
        value.setValue("666");
        StoreRouteNode child = new StoreRouteNode();
        child.setValue(value);
        root.appendChild("com",child);
        System.out.println(root.findChild("com").getValue().getValue());
        root.setValue(new Value());
        child.appendChild("guigu",new StoreRouteNode());
        System.out.println(root.getValue());
        System.out.println(child.getChildren());
    }
}