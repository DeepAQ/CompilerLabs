package cn.imaq.lex;

import cn.imaq.lex.dfa.DFA;
import cn.imaq.lex.dfa.DFAState;
import cn.imaq.lex.nfa.NFA;
import cn.imaq.lex.nfa.NFABlock;
import cn.imaq.lex.re.RE;
import cn.imaq.lex.util.Debug;
import cn.imaq.lex.util.LexException;

public class Main {
    public static void main(String[] args) {
        Debug.debug = true;
        try {
            NFABlock nfa = NFA.fromRE(RE.parse("aa*((bab*a)*(a|b)b*)*"));
//            NFABlock nfa = NFA.fromRE(RE.parse("(a|b)*"));
            DFAState dfa = DFA.fromNFA(nfa);
        } catch (LexException e) {
            e.printStackTrace();
        }
    }
}
