package com.github.xlljc.template.store;

import com.github.xlljc.template.interfaces.RouteNode;
import com.github.xlljc.template.interfaces.Store;
import com.github.xlljc.utils.RegexUtil;

import java.util.Map;

public class DataStore implements Store {

    /**
     *
     */
    private final RouteNode varRouteNode = new StoreRouteNode();

    private final RouteNode localVarRouteNode = new StoreRouteNode();

    /**
     * 是否已经上锁
     */
    private boolean block = false;

    @Override
    public void declareVar(String route, Object value) {

    }

    @Override
    public void declareReadonlyVar(String route, Object value) {

    }

    @Override
    public void declareLocalVar(String route, Object value) {

    }

    @Override
    public void declareLocalReadonlyVar(String route, Object value) {

    }

    @Override
    public Object getVal(String route) {
        Value value = analysisRoute(route);
        if (value != null) return value.getValue();
        return null;
    }

    @Override
    public void setVal(String route, Object value) {
        Value valueObj = analysisRoute(route);
        if (valueObj == null) {
            //抛出异常
            System.err.println("使用了未声明的路由: " + route);
            return;
        }
        if (valueObj.getAccess() == Value.Access.readOnly) {
            //抛出异常
            System.err.println("不能修改只读路由: " + route);
            return;
        }

    }

    @Override
    public void lock() {
        block = true;
    }

    @Override
    public Store copy() {
        return null;
    }

    /**
     * 解析路由, 并从各存储对象中找到对应的值
     * @param route 路由地址, 例如 aaa.bbb
     */
    private Value analysisRoute(String route) {
        //验证路由是合法, 只能是 xxx.xxx.xxx 这种格式
        if (!RegexUtil.matches("^\\w+(\\.\\w+)*$", route)) {
        //if (!RegexUtil.matches("^\\w+(((\\.\\w+)*)|((\\.\\w+)*\\(((('[^n]*')|(\\w+(\\.\\w+)*))( *, *(('[^n]*')|(\\w+(\\.\\w+)*)))*)?\\)))$", route)) {
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

}
