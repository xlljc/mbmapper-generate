package com.github.xlljc;


import com.github.xlljc.config.MbMapperConfig;
import com.github.xlljc.config.MbMapperConfigException;
import com.github.xlljc.produce.template.MbMapperTemplateException;
import com.github.xlljc.produce.template.Template;
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
    void test3() throws IOException, MbMapperTemplateException {
        String s = FileUtil.loadFile(new File("H:\\idea\\mapTest\\template.ctp"));
        //Matcher matcher = RegexUtil.matcher("(<#.+>)|((?<!\\\\)\\$\\{.+})", s);
        System.out.println("----------------------------- 源代码: \n" + s);
        Template template = new Template(s);
        String code = template.conversion();
        System.out.println("----------------------------- 最终代码: -----------------------------\n" + code);
    }

    public static String eachTarget(String template, int layer) throws MbMapperTemplateException {
        StringBuilder code = new StringBuilder(template);
        System.out.println("----------------------- eachTarget " + layer + " ----------------------");
        System.out.println("参数: \n" + template);

        //循环匹配最外层标签
        boolean bk = true;
        while (bk) {

            Matcher matcher = RegexUtil.matcher("(<#\\s*[\\w\\-]+\\s*( +[\\w=\\- \\n]+(\"[^\"\\n]*\")?)*\\s*>)|(</#\\s*[\\w\\-]+\\s*>)", template);
            int offset = 0;
            if (matcher.find()) {
                //如果自己匹配到闭标签, 就抛出异常
                String closeTarget = matcher.group(4);
                if (closeTarget != null) {
                    //抛出异常
                    System.err.println("发现未成对的闭标签: " + closeTarget);
                    break;
                }
                //开标签
                String openTarget = matcher.group(1);
                System.out.println("<>: " + openTarget);
                //获取标签名称
                String name = RegexUtil.findFirst("(?<=<#)[\\w\\-]+", openTarget);
                if (name != null) {
                    //Target target = Target.Load(IfTarget.class, openTarget);
                    //System.out.println("name: " + name + ", target: " + target);
                    //记录位置
                    int start = matcher.start();

                    Matcher targetMatcher = RegexUtil.matcher(
                            String.format("(<#\\s*%s\\s*( +[\\w=\\- \\n]+(\"[^\"\\n]*\")?)*\\s*>)|(</#\\s*%s\\s*>)", name, name), template);
                    targetMatcher.find(start);

                    int flag = 1, end = -1;
                    //寻找对应的闭标签
                    while (targetMatcher.find()) {
                        //开标签
                        String g1 = targetMatcher.group(1);
                        //闭标签
                        String g2 = targetMatcher.group(4);
                        if (g1 != null) flag++;
                        else if (g2 != null) flag--;
                        //达到平衡了就可以出去了
                        if (flag == 0) {
                            end = targetMatcher.end();
                            break;
                        }
                    }

                    if (end == -1) {   //如果没找到对应的闭标签
                        System.out.println("发现未成对的开标签: " + openTarget);
                        System.out.println(targetMatcher);
                        //抛出异常

                    } else {
                        //标签字符串
                        String targetStr = template.substring(start, end);
                        String bodyStr = targetStr.replaceAll(
                                String.format("(^<#\\s*%s\\s*( +[\\w=\\- \\n]+(\"[^\"\\n]*\")?)*\\s*>[\\t ]*\\n?)|(\\n?[\\t ]*</#\\s*%s\\s*>$)", name, name), "");

                        System.out.println("标签: \n" + targetStr);
                        System.out.println("内容: \n" + bodyStr);

                        //递归
                        String resultStr = eachTarget(bodyStr, layer + 1);

                        System.out.println("result: " + resultStr);
                    /*System.out.println(String.format("start: %s, end: %s, offset: %s, length: %s, resultStr: %s, targetStr: %s",
                            start, end, offset, code.length(), resultStr.length(), targetStr.length()));*/

                        //替换
                        code.replace(start - offset, end - offset, resultStr);
                        offset += targetStr.length() - resultStr.length();
                    }
                } else {
                    System.out.println("未取得名称!");
                }
            }

        }

        System.out.println("--------------------- close " + layer + " --------------------");
        return code.toString();
    }



    @Test
    void test4() {

    }
}
