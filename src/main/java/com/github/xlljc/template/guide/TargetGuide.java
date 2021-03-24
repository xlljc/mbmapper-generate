package com.github.xlljc.template.guide;

import com.github.xlljc.template.target.Target;

import java.util.HashMap;
import java.util.Map;

public class TargetGuide {

    private final Map<String, Class<? extends Target>> storage;

    public TargetGuide() {
        storage = new HashMap<>();
    }

    public Map<String, Class<? extends Target>> getStorage() {
        return storage;
    }

    public Class<? extends Target> findTarget(String name) {
        Class<? extends Target> targetClass = storage.get(name);
        if (targetClass == null) {
            System.err.println("没有找到标签: " + name);
            return null;
        }
        return targetClass;
    }

    public static TargetGuide initGuide(Registry ...registry) {
        TargetGuide targetGuide = new TargetGuide();
        for (Registry item : registry) {
            targetGuide.storage.putAll(item.registryTargets());
        }
        return targetGuide;
    }

}
