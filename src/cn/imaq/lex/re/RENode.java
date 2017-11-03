package cn.imaq.lex.re;

public class RENode {
    public Type type;

    public char ch;

    RENode(Type type, char ch) {
        this.type = type;
        this.ch = ch;
    }

    public enum Type {
        CH, OP
    }
}
