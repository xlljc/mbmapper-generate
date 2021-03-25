package com.github.xlljc.template.guide;

import com.github.xlljc.template.interfaces.Guide;
import com.github.xlljc.template.interfaces.Registry;
import com.github.xlljc.template.target.Target;

import java.util.HashMap;
import java.util.Map;

public class TargetGuide implements Guide<Class<? extends Target>> {

    private final Map<String, Class<? extends Target>> storage;

    public TargetGuide() {
        storage = new HashMap<>();
    }

    @Override
    public void registry(Registry<Class<? extends Target>> registry) {
        storage.putAll(registry.registry());
    }

    @Override
    public void registries(Registry<Class<? extends Target>>[] registries) {
        for (Registry<Class<? extends Target>> registry : registries) {
            storage.putAll(registry.registry());
        }
    }

    @Override
    public Class<? extends Target> find(String describe) {
        Class<? extends Target> targetClass = storage.get(describe);
        if (targetClass == null) {
            System.err.println("没有找到标签: " + describe);
            return null;
        }
        return targetClass;
    }
}
