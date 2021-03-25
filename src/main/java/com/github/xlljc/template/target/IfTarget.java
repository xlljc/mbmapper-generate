package com.github.xlljc.template.target;

public class IfTarget extends Target {

    protected String test;

    public IfTarget(String targetName) {
        super(targetName);
    }

    @Override
    public String beforeProcess(String content) {
        return content;
    }

    @Override
    public String process(String content) {
        return content + ",,,";
    }


}
