package com.github.xlljc.template.interfaces;

import com.github.xlljc.template.store.Value;

import java.util.Map;

/**
 * 路由节点, 路由的最小单位
 */
public interface RouteNode {

    /**
     * 根据子节点名称寻找子路由子节点, 如果没有找到就返回null
     * @param childName 子节点名称
     */
    RouteNode findChild(String childName);

    /**
     * 根据节点名称创建子级路由节点, 返回创建的节点, 如果该路由已经存在, 就抛出异常
     * @param childName 子节点名称
     */
    void appendChild(String childName, RouteNode routeNode);

    /**
     * 获取所有子级路由节点
     */
    Map<String, RouteNode> getChildren();

    /**
     * 获取当前节点绑定的值
     */
    Value getValue();

    /**
     * 设置当前节点绑定的值, 当value的值为Map时设置值会抛出异常
     */
    void setValue(Value value);

    /**
     * 返回当前路由节点是否还是空节点
     */
    boolean isEmpty();

    /**
     * 返回当前路由节点是否是值节点
     */
    boolean isValueNode();

    /**
     * 返回当前路由节点是否是路由节点
     */
    boolean isRouteNode();

}
