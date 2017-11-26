package cn.imaq.yuck.util;

public class IDGen {
    private int id = 0;

    public int next() {
        return id++;
    }

    public int get() {
        return id;
    }
}
