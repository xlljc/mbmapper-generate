package com.github.xlljc.template.guide;

import com.github.xlljc.template.target.Target;

import java.util.Map;

public interface Registry {

    /**
     * 注册节点
     * @return 名称 - Class对象 键值对
     */
    Map<String, Class<? extends Target>> registryTargets();

}
