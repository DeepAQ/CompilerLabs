package cn.imaq.lex.nfa;

public class NFABlock {
    NFAState start, end;

    public NFABlock(NFAState start, NFAState end) {
        this.start = start;
        this.end = end;
    }
}
