package cn.imaq.lex.dfa;

import cn.imaq.lex.nfa.NFABlock;
import cn.imaq.lex.nfa.NFAState;
import cn.imaq.lex.util.Debug;
import cn.imaq.lex.util.IDGen;

import java.util.*;

public class DFA {
    public static DFAState fromNFA(NFABlock nfa) {
        IDGen id = new IDGen();
        List<DFAState> states = new ArrayList<>();
        // Add I_0
        states.add(new DFAState(id.next(), nfa.start.ec()));
        for (int i = 0; i < states.size(); i++) {
            DFAState state = states.get(i);
            Map<Character, Set<NFAState>> moves = new HashMap<>();
            for (NFAState nfaState : state.nfaStates) {
                for (Character c : nfaState.next.keySet()) {
                    if (c != null) {
                        if (!moves.containsKey(c)) {
                            moves.put(c, new HashSet<>());
                        }
                        for (NFAState nextNFAState : nfaState.next.get(c)) {
                            moves.get(c).addAll(nextNFAState.ec());
                        }
                    }
                }
            }
            for (Map.Entry<Character, Set<NFAState>> entry : moves.entrySet()) {
                DFAState nextState = new DFAState(entry.getValue());
                int index = states.indexOf(nextState);
                if (index >= 0) {
                    nextState = states.get(index);
                } else {
                    nextState.id = id.next();
                    for (NFAState nfaState : entry.getValue()) {
                        nextState.accept = nextState.accept || nfaState.equals(nfa.end);
                    }
                    states.add(nextState);
                }
                state.next.put(entry.getKey(), nextState);
            }
            // DEBUG
            if (Debug.debug) {
                for (int j = 0; j < states.size(); j++) {
                    System.out.print("DFAState[" + j + "]: " + Arrays.toString(states.get(j).nfaStates.stream().map(s -> s.id).toArray()) + " ");
                    for (Character nextChar : states.get(j).next.keySet()) {
                        System.out.print(nextChar + " -> " + states.get(j).next.get(nextChar).id + ", ");
                    }
                    if (states.get(j).accept) {
                        System.out.print("[Accept]");
                    }
                    System.out.println();
                }
                System.out.println();
            }
        }
        return states.get(0);
    }
}
