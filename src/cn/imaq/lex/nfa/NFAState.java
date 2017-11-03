package cn.imaq.lex.nfa;

import cn.imaq.lex.util.MultiMap;

public class NFAState {
    int id;
    MultiMap<Character, NFAState> next;

    public NFAState(int id) {
        this.id = id;
        next = new MultiMap<>();
    }

    public NFAState(int id, MultiMap<Character, NFAState> next) {
        this.id = id;
        this.next = next;
    }

    @Override
    public String toString() {
        return "NFAState{" + id + '}';
    }
}
