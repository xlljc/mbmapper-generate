package com.github.xlljc.template.target;

import com.github.xlljc.template.annotation.Necessary;
import com.github.xlljc.template.interfaces.Store;

public class IfTarget extends Target {

    @Necessary
    protected String test;

    public IfTarget(String targetName) {
        super(targetName);
    }

    @Override
    public String beforeProcess(String content, Store store) {

        return content;
    }

    @Override
    public String process(String content, Store store) {
        return content + ",,,";
    }


}
