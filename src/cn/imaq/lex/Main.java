package cn.imaq.lex;

import cn.imaq.lex.dfa.DFA;
import cn.imaq.lex.dfa.DFAState;
import cn.imaq.lex.nfa.NFA;
import cn.imaq.lex.nfa.NFAState;
import cn.imaq.lex.re.RE;
import cn.imaq.lex.util.Debug;
import cn.imaq.lex.util.IDGen;
import cn.imaq.lex.util.LexException;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        Debug.debug = true;
        try {
            IDGen id = new IDGen();
            NFAState nfa = NFA.merge(Arrays.asList(
                    NFA.fromRE("op1", RE.parse("+|-"), id),
                    NFA.fromRE("op2", RE.parse("++|--"), id),
                    NFA.fromRE("op3", RE.parse("+=|-="), id)
            ), id);
            DFAState dfa = DFA.fromNFA(nfa);
        } catch (LexException e) {
            e.printStackTrace();
        }
    }
}
