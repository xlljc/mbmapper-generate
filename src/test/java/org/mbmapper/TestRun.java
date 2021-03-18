package org.mbmapper;


import org.junit.jupiter.api.Test;
import org.mbmapper.config.MbMapperConfig;
import org.mbmapper.config.MbMapperConfigException;
import org.mbmapper.produce.template.MbMapperTemplateException;
import org.mbmapper.produce.template.Target;
import org.mbmapper.produce.template.target.IfTarget;
import org.mbmapper.utils.FileUtil;
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
        //Matcher matcher = RegexUtil.matcher("(<#.+>)|((?<!\\\\)\\$\\{.+})", s);
        String code = eachTarget(s, 0);
        System.out.println("----------------------------- 最终代码: -----------------------------\n" + code);
    }

    public static String eachTarget(String template, int layer) throws MbMapperTemplateException {
        StringBuilder code = new StringBuilder();
        System.out.println("----------------------- eachTarget " + layer + " ----------------------");
        Matcher matcher = RegexUtil.matcher("(<#\\s*[\\w\\-]+\\s*( *[\\w=\\- \\n]+(\"[^\"\\n]*\")?)*\\s*>)|(</#\\s*[\\w\\-]+\\s*>)", template);

        int index = 0;
        while (matcher.find()) {
            index++;
            //如果自己匹配到闭标签, 就抛出异常
            String closeTarget = matcher.group(4);
            if (closeTarget != null) {
                //抛出异常
                //System.out.println("发现未成对的闭标签: " + closeTarget);
                //break;
                continue;
            }
            String openTarget = matcher.group(1);
            System.out.println("<>: " + openTarget);
            //获取标签名称
            String name = RegexUtil.findFirst("(?<=<#)[\\w\\-]+", openTarget);
            if (name != null) {
                Target target = Target.Load(IfTarget.class, openTarget);
                //System.out.println("name: " + name + ", target: " + target);
                //记录位置,到时候还要回滚
                int start = matcher.start();

                Matcher targetMatcher = RegexUtil.matcher(
                        String.format("(<#\\s*%s\\s*([\\w=\\- \\n]+( *\"[^\"\\n]*\")?)*\\s*>)|(</#\\s*%s\\s*>)", name, name), template);
                targetMatcher.find(start);
                //起始和关闭位置
                int flag = 1, end = -1;
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
                    //抛出异常

                } else {
                    //标签字符串
                    String targetStr = template.substring(start, end);
                    String bodyStr = targetStr.replaceAll(
                            String.format("(^<#\\s*%s\\s*([\\w=\\- \\n]+( *\"[^\"\\n]*\")?)*\\s*>[\\t ]*\\n?)|(\\n?[\\t ]*</#\\s*%s\\s*>$)", name, name),"");


                    System.out.println("结果: \n" + targetStr);

                    //递归
                    code.append(eachTarget(bodyStr, layer + 1));

                }

                //matcher.find(start);
            } else {
                System.out.println("未取得名称!");
            }
        }
        if (index == 0) {
            code.append(template);
        }
        return code.toString();
    }



    @Test
    void test4() {

    }
}
