package cn.imaq.lex.util;

public class IDGen {
    private int id = 0;

    public int next() {
        return id++;
    }

    public int get() {
        return id;
    }
}
