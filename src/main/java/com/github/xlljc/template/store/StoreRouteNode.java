package com.github.xlljc.template.store;

import com.github.xlljc.template.interfaces.RouteNode;

import java.util.HashMap;
import java.util.Map;

public class StoreRouteNode implements RouteNode {

    private Value value = new Value();

    @Override
    public RouteNode findChild(String childName) {
        if (!isEmpty()) {
            if (isRouteNode()) {
                return ((Map<String, RouteNode>) value.getValue()).get(childName);
            } else {
                System.err.println("当前路由无法获取子路由, 因为它是值节点!");
            }
        } else {
            System.err.println("当前路由无法获取子路由, 因为它是空节点!");
        }
        return null;
    }

    @Override
    public void appendChild(String childName, RouteNode routeNode) {
        Map<String, RouteNode> map;
        if (isEmpty()) { //还没设置过值
            map = new HashMap<>();
            value.setValue(map);
        } else if (isRouteNode()) { //已经被设置过值, 且不是值节点
            map = (Map<String, RouteNode>) value.getValue();
        } else { //是值节点, 不能创建子节点
            System.err.println("无法创建子级路由, 因为他是值路由");
            return;
        }
        map.put(childName, routeNode);
    }

    @Override
    public Map<String, RouteNode> getChildren() {
        if (isEmpty() || isValueNode()) {
            System.err.println("无法获取子级节点, 因为它不是节点路由");
            return null;
        }
        return (Map<String, RouteNode>) value.getValue();
    }

    @Override
    public Value getValue() {
        if (isEmpty() || isRouteNode()) {
            System.err.println("无法获取节点值, 因为它不是值节点");
            return null;
        }
        return value;
    }

    @Override
    public void setValue(Value value) {
        if (isRouteNode()) {
            System.err.println("无法设置值给当前路由, 因为它不是值节点!");
        } else if (value.getValue() instanceof Map) {
            System.err.println("不能设置值类型为Map, 这样会导致冲突!");
        } else {
            this.value.setValue(value.getValue());
            this.value.setAccess(value.getAccess());
        }
    }

    @Override
    public boolean isEmpty() {
        return value.getValue() == null;
    }

    @Override
    public boolean isValueNode() {
        return !isRouteNode();
    }

    @Override
    public boolean isRouteNode() {
        return value.getValue() instanceof Map;
    }
}
