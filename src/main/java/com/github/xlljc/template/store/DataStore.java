package com.github.xlljc.template.store;

import com.github.xlljc.template.interfaces.RouteNode;
import com.github.xlljc.template.interfaces.Store;
import com.github.xlljc.utils.RegexUtil;

import java.lang.reflect.Field;
import java.util.*;


public class DataStore implements Store {

    /**
     *
     */
    private final RouteNode varRouteNode = new StoreRouteNode();

    private final RouteNode localVarRouteNode = new StoreRouteNode();

    /**
     * 是否已经上锁
     */
    private boolean lock = false;

    @Override
    public void declareVar(String route, Object value) {
        String[] paths = getRoutePaths(route);
        RouteNode temp = varRouteNode;
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            if (temp.isEmpty()) { //没有的话就创建
                RouteNode routeNode = new StoreRouteNode();
                temp.appendChild(path, routeNode);
                temp = routeNode;
            } else if (temp.isRouteNode()) {
                RouteNode node = temp.findChild(path);
                if (node == null) {
                    node = new StoreRouteNode();
                    temp.appendChild(path, node);
                }
                temp = node;
            } else {
                System.err.println("当前路由无法获取子路由, 因为它是值节点!");
                return;
            }
        }
        if (!temp.isValueNode()) {
            temp.setValue(new Value(value));
        } else {
            System.err.println("重复定义路由: " + route);
        }
    }

    @Override
    public void declareReadonlyVar(String route, Object value) {
        String[] paths = getRoutePaths(route);
        RouteNode temp = varRouteNode;
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            if (temp.isEmpty()) { //没有的话就创建
                RouteNode routeNode = new StoreRouteNode();
                temp.appendChild(path, routeNode);
                temp = routeNode;
            } else if (temp.isRouteNode()) {
                RouteNode node = temp.findChild(path);
                if (node == null) {
                    node = new StoreRouteNode();
                    temp.appendChild(path, node);
                }
                temp = node;
            } else {
                System.err.println("当前路由无法获取子路由, 因为它是值节点!");
                return;
            }
        }
        if (!temp.isValueNode()) {
            temp.setValue(new Value(Value.Access.readOnly, value));
        } else {
            System.err.println("重复定义路由: " + route);
        }
    }

    @Override
    public void declareLocalVar(String route, Object value) {

    }

    @Override
    public void declareLocalReadonlyVar(String route, Object value) {

    }

    @Override
    public Object getVal(String route) {
        String[] paths = getRoutePaths(route);
        boolean flag = false;
        RouteNode temp = varRouteNode;
        Object obj = null;
        for (String path : paths) {
            //拿取路由
            if (!flag) {
                if (temp.isValueNode()) { //路由到尽头了, 变成普通对象
                    obj = temp.getValue().getValue();
                    flag = true;
                } else temp = temp.findChild(path);
            }
            //拿取对象属性
            if (flag) {
                Field field;
                try {
                    //获取所有字段, 包括父类继承的
                    field = findField(obj.getClass(), path);
                    field.setAccessible(true);
                    obj = field.get(obj);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        if (flag)
            return obj;
        return temp == null ? null : temp.getValue().getValue();
    }

    @Override
    public void setVal(String route, Object value) {
        String[] paths = getRoutePaths(route);
        boolean flag = false;
        RouteNode temp = varRouteNode;
        Object obj = null;
        String lastPath = "";
        for (String path : paths) {
            if (flag) { //拿取对象属性
                Field field;
                try {
                    //获取所有字段, 包括父类继承的
                    field = findField(obj.getClass(), path);
                    field.setAccessible(true);
                    obj = field.get(obj);
                } catch (NoSuchFieldException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            if (!flag) { //拿取路由
                if (temp.isValueNode()) { //路由到尽头了, 变成普通对象
                    obj = temp.getValue().getValue();
                    flag = true;
                    lastPath = path;
                } else temp = temp.findChild(path);
            }
        }
        if (flag) { //反射设置对象属性
            try {
                Field field = findField(obj.getClass(), lastPath);
                field.setAccessible(true);
                field.set(obj , value);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                e.printStackTrace();
            }
        } else { //设置节点值
            Value val = temp.getValue();
            if (!lock || val.getAccess() == Value.Access.writeAndRead) { //没上锁或者属性不是readonly
                val.setValue(value);
            } else {
                System.err.println("不能修改当前属性: " + route + ", 因为它是只读的!");
            }
        }
    }

    @Override
    public void lock() {
        lock = true;
    }

    @Override
    public Store copy() {
        return null;
    }

    /**
     * 打印仓库的路由数据
     */
    public void printData() {
        System.out.println("varRouteNode: => ");
        String s = _printData(varRouteNode, 1);
        System.out.println(s);
    }

    /**
     * 解析路由, 并从各存储对象中找到对应的值
     *
     * @param route 路由地址, 例如 aaa.bbb
     */
    private Value analysisRoute(String route) {
        //验证路由是合法, 只能是 xxx.xxx.xxx 这种格式
        if (!isOnlyPath(route)) {
            System.err.println("不合法的路由: " + route);
            return null;
        }
        String[] paths = route.split("\\.");
        RouteNode tempNode = varRouteNode;
        for (int i = 0; i < paths.length; i++) {
            String path = paths[i];
            if (tempNode.isEmpty()) {
                RouteNode temp = new StoreRouteNode();
                tempNode.appendChild("path", temp);
            } else if (tempNode.isRouteNode()) { //是否是路由节点
                tempNode = tempNode.findChild(path);
            } else { //是值节点
                if (i == paths.length - 1) {
                    return tempNode.getValue();
                } else {
                    System.err.println("错误的路由: " + path + ", index: " + i);
                    break;
                }
            }
            RouteNode child = tempNode.findChild(path);

        }

        return null;
    }


    private boolean isOnlyPath(String routeName) {
        //if (!RegexUtil.matches("^\\w+(((\\.\\w+)*)|((\\.\\w+)*\\(((('[^n]*')|(\\w+(\\.\\w+)*))( *, *(('[^n]*')|(\\w+(\\.\\w+)*)))*)?\\)))$", route)) {
        return RegexUtil.matches("^\\w+(\\.\\w+)*$", routeName);
    }

    private String[] getRoutePaths(String route) {
        //验证路由是合法, 只能是 xxx.xxx.xxx 这种格式
        if (!isOnlyPath(route)) {
            System.err.println("不合法的路由: " + route);
            return null;
        }
        return route.split("\\.");
    }

    private String _printData(RouteNode routeNode, int layer) {
        StringBuilder sb = new StringBuilder();
        if (routeNode != null && !routeNode.isEmpty()) {
            if (routeNode.isRouteNode()) {
                Map<String, RouteNode> children = routeNode.getChildren();
                Set<String> set = children.keySet();
                for (String s : set) {
                    RouteNode node = children.get(s);
                    sb.append(loopStr("    ", layer)).append("route: ").append(s).append("\n");
                    sb.append(_printData(node, layer + 1));
                }
            } else {
                Value value = routeNode.getValue();
                sb.append(loopStr("    ", layer)).append("access: ").append(value.getAccess())
                        .append(", value: ").append(value.getValue()).append("\n");
            }
        }
        return sb.toString();
    }

    private StringBuilder loopStr(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb;
    }

    /**
     * 寻找字段, 找不到就一直往父类找
     */
    private Field findField(Class<?> cls, String name) throws NoSuchFieldException {
        Field field;
        try {
            field = cls.getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            Class<?> superclass = cls.getSuperclass();
            if (superclass != null) {
                field = findField(superclass, name);
            } else {
                throw e;
            }
        }
        return field;
    }

}
