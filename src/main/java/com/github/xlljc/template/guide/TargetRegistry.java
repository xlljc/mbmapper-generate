package com.github.xlljc.template.guide;

import com.github.xlljc.template.target.IfTarget;
import com.github.xlljc.template.target.Target;

import java.util.HashMap;
import java.util.Map;

/**
 * 内置的节点注册机
 */
public class TargetRegistry implements Registry<Class<? extends Target>> {

    @Override
    public Map<String, Class<? extends Target>> registry() {
        Map<String, Class<? extends Target>> classMap = new HashMap<>();
        classMap.put("if", IfTarget.class);
        return classMap;
    }
}
