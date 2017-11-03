package cn.imaq.lex.analyze;

import cn.imaq.lex.dfa.DFAState;

public class Analyzer {
    public static void analyze(String target, DFAState dfa) {
        StringBuilder current = new StringBuilder();
        DFAState state = dfa;
        for (char c : target.toCharArray()) {
            if (c == ' ' || c == '\t' || c == '\r' || c == '\n') {
                if (current.length() > 0) {
                    if (state.tag != null) {
                        System.out.println("[" + state.tag + "] " + current);
                    } else {
                        System.out.println("[Warning] Could not recogize \"" + current + "\" as any tag");
                    }
                    state = dfa;
                    current = new StringBuilder();
                }
                continue;
            }
            if (state.next.containsKey(c)) {
                current.append(c);
                state = state.next.get(c);
            } else {
                if (state.tag != null) {
                    System.out.println("[" + state.tag + "] " + current);
                }
                state = dfa;
                current = new StringBuilder();
                if (state.next.containsKey(c)) {
                    state = state.next.get(c);
                    current.append(c);
                } else {
                    System.out.println("[Warning] Could not recogize \"" + c + "\" as any tag");
                }
            }
        }
    }
}
