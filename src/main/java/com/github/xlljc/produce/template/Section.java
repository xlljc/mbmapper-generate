package com.github.xlljc.produce.template;

public class Section {

    private int start;
    private int end;

    public Section() {}

    public int getStart() {
        return start;
    }

    public Section(int start, int end) {
        this.start = start;
        this.end = end;
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
}
