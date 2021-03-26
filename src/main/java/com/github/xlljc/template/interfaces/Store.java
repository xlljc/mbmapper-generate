package com.github.xlljc.template.interfaces;

public interface Store {


    void declareVar(String route, Object value);


    void declareReadonlyVar(String route, Object value);


    void declareLocalVar(String route, Object value);


    void declareLocalReadonlyVar(String route, Object value);


    Object getVal(String route);


    void setVal(String route, Object value);


    void lock();

    /**
     * 复制一份仓库, 内容不变, 全局变量会被保存下来
     * @return 新的仓库
     */
    Store copy();

}
