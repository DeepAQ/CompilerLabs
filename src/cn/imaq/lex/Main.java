package cn.imaq.lex;

import cn.imaq.lex.analyze.Analyzer;
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
                    NFA.fromRE("op1", RE.parse("+|-|\\*|/"), id),
                    NFA.fromRE("op2", RE.parse("++|--"), id),
                    NFA.fromRE("op3", RE.parse("+=|-=|\\*=|/="), id),
                    NFA.fromRE("number", RE.parse("(0|1|2|3|4|5|6|7|8|9)(0|1|2|3|4|5|6|7|8|9)*"), id)
            ), id);
            DFAState dfa = DFA.fromNFA(nfa);
            Analyzer.analyze("abc +== -345; i += 123; j++; k = i * j / k;", dfa);
        } catch (LexException e) {
            e.printStackTrace();
        }
    }
}
