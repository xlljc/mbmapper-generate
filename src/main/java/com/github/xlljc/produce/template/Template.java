package com.github.xlljc.produce.template;

import com.github.xlljc.utils.RegexUtil;

import java.util.regex.Matcher;

public class Template {

    private final String template;

    public Template(String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    public String conversion() {
        return _conversion(0).toString();
    }


    private StringBuilder _conversion(int layer) {
        StringBuilder template = new StringBuilder(this.template);
        boolean eachFlag = true;
        while(eachFlag) {
            //查找第一个标签的位置
            Section section = findFirstTarget(template);
            if (section.getStart() > 0) {
                String target = template.substring(section.getStart(), section.getEnd());
                String content = unwrap(target);

                String targetName = regexTargetName(target);
                //处理标签

                StringBuilder result = new Template(content)._conversion(layer + 1);
                template.replace(section.getStart(), section.getEnd(), result.toString());
            } else {
                //如果没有, 就结束循环
                eachFlag = false;
            }
        }

        return template;
    }

    /**
     * 找到第一个最外层标签, 返回起始点
     */
    private Section findFirstTarget(StringBuilder template) {
        Section section = new Section(-1, -1);
        Matcher openMatcher = regexTarget(template.toString());
        if (openMatcher.find()) { //找到了, 截取标签
            //如果自己匹配到闭标签, 就抛出异常
            String closeTarget = openMatcher.group(4);
            if (closeTarget != null) {
                //抛出异常
                System.err.println("发现未成对的闭标签: " + closeTarget);
                return null;
            }
            //如果是单标签
            String singleTarget = openMatcher.group(5);
            if (singleTarget != null) {
                section.setStart(openMatcher.start(5));
                section.setEnd(openMatcher.end(5));
                return section;
            }
            //如果是开标签, 则继续
            String openTarget = openMatcher.group(1);
            String targetName = regexTargetName(openTarget);
            int start = openMatcher.start(1);
            Matcher closeMatcher = regexTargetByName(template.toString(), targetName);
            closeMatcher.find(start);
            int flag = 1, end = -1;
            while (closeMatcher.find()) {
                //开标签
                String g1 = closeMatcher.group(1);
                //闭标签
                String g2 = closeMatcher.group(4);
                if (g1 != null) flag++;
                else if (g2 != null) flag--;
                //达到平衡了就可以出去了
                if (flag == 0) {
                    section.setStart(start);
                    section.setEnd(closeMatcher.end());
                    return section;
                }
            }
        }
        return section;
    }

    /**
     * 返回一个匹配标签的Matcher类
     */
    private Matcher regexTarget(String template) {
        //匹配开始标签,结束标签,以及单标签
        return RegexUtil.matcher("(<#\\s*[\\w\\-]+\\s*( +[\\w=\\- \\n]+(\"[^\"\\n]*\")?)*\\s*>)|(</#\\s*[\\w\\-]+\\s*>)|(<#\\s*[\\w\\-]+\\s*( +[\\w=\\- \\n]+(\"[^\"\\n]*\")?)*\\s*/>)", template);
    }

    /**
     * 根据标签名匹配一个
     */
    private Matcher regexTargetByName(String template, String targetName) {
        return RegexUtil.matcher(
                String.format("(<#\\s*%s\\s*( +[\\w=\\- \\n]+(\"[^\"\\n]*\")?)*\\s*>)|(</#\\s*%s\\s*>)", targetName, targetName), template);
    }

    /**
     * 返回匹配标签的名称,如果没有没有匹配到名称, 就返回null
     */
    private String regexTargetName(String openTarget) {
        return RegexUtil.findFirst("(?<=<#)[\\w\\-]+", openTarget);
    }

    /**
     * 将标签解包
     */
    private String unwrap(String target) {
        return target.replaceAll(
                "(^<#\\s*[\\w\\-]+\\s*( +[\\w=\\- \\n]+(\"[^\"\\n]*\")?)*\\s*>[\\t ]*\\n?[\\t ]*)|([\\t ]*\\n?[\\t ]*</#\\s*[\\w\\-]+\\s*>$)|(^<#\\s*[\\w\\-]+\\s*( +[\\w=\\- \\n]+(\"[^\"\\n]*\")?)*\\s*/>$)", "");
                //"(^<#\\s*[\\w\\-]+\\s*( +[\\w=\\- \\n]+(\"[^\"\\n]*\")?)*\\s*>[\\t ]*\\n?)|(\\n?[\\t ]*</#\\s*[\\w\\-]+\\s*>$)|(^<#\\s*[\\w\\-]+\\s*( +[\\w=\\- \\n]+(\"[^\"\\n]*\")?)*\\s*>$)", "");
    }

}
