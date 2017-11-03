package cn.imaq.lex.nfa;

public class NFABlock {
    public NFAState start, end;

    NFABlock(NFAState start, NFAState end) {
        this.start = start;
        this.end = end;
    }
}
