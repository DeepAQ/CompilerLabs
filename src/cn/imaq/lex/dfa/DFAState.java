package cn.imaq.lex.dfa;

import cn.imaq.lex.nfa.NFAState;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DFAState {
    int id = -1;
    boolean accept = false;
    Set<NFAState> nfaStates;
    Map<Character, DFAState> next = new HashMap<>();

    public DFAState(Set<NFAState> nfaStates) {
        this.nfaStates = nfaStates;
    }

    public DFAState(int id, Set<NFAState> nfaStates) {
        this.id = id;
        this.nfaStates = nfaStates;
    }

    public DFAState(int id, Set<NFAState> nfaStates, boolean accept) {
        this.id = id;
        this.nfaStates = nfaStates;
        this.accept = accept;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DFAState state = (DFAState) o;

        return nfaStates != null ? nfaStates.equals(state.nfaStates) : state.nfaStates == null;
    }

    @Override
    public int hashCode() {
        return nfaStates != null ? nfaStates.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "DFAState{" +
                "id=" + id +
                ", accept=" + accept +
                ", nfaStates=" + nfaStates +
                '}';
    }
}
