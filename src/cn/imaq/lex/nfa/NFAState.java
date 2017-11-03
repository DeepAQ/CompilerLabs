package cn.imaq.lex.nfa;

import cn.imaq.lex.util.MultiMap;

import java.util.HashSet;
import java.util.Set;

public class NFAState {
    public int id;
    public MultiMap<Character, NFAState> next;

    NFAState(int id) {
        this.id = id;
        next = new MultiMap<>();
    }

    public Set<NFAState> ec() {
        Set<NFAState> result = new HashSet<>();
        addEcTo(result);
        return result;
    }

    private void addEcTo(Set<NFAState> target) {
        if (target.contains(this)) {
            return;
        }
        target.add(this);
        for (NFAState eNext : next.get(null)) {
            eNext.addEcTo(target);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NFAState state = (NFAState) o;

        return id == state.id;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public String toString() {
        return "NFAState{" + id + '}';
    }
}
