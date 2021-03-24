package com.github.xlljc.template;


public class TargetResult {

    /**
     * 起始位置
     */
    private int start = -1;
    /**
     * 结束位置
     */
    private int end = -1;
    /**
     * 标签名称
     */
    private String name;
    /**
     * 开标签
     */
    private String openTarget;
    /**
     * 标签 + 内容
     */
    private String target;
    /**
     * 标签内容
     */
    private String content;
    /**
     * 是否是单标签
     */
    private boolean single = false;

    public TargetResult() {
    }

    public TargetResult(int start, int end, String name, String openTarget, String target, String content, boolean single) {
        this.start = start;
        this.end = end;
        this.name = name;
        this.openTarget = openTarget;
        this.target = target;
        this.content = content;
        this.single = single;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOpenTarget() {
        return openTarget;
    }

    public void setOpenTarget(String openTarget) {
        this.openTarget = openTarget;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSingle() {
        return single;
    }

    public void setSingle(boolean single) {
        this.single = single;
    }
}
