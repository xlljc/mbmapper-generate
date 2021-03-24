package com.github.xlljc.template.guide;

import com.github.xlljc.template.target.IfTarget;
import com.github.xlljc.template.target.Target;

import java.util.HashMap;
import java.util.Map;

public class TargetRegistry implements Registry {

    @Override
    public Map<String, Class<? extends Target>> registryTargets() {
        Map<String, Class<? extends Target>> classMap = new HashMap<>();
        classMap.put("if", IfTarget.class);
        return classMap;
    }
}
