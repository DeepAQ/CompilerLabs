package cn.imaq.lex.nfa;

import cn.imaq.lex.re.RENode;
import cn.imaq.lex.util.Debug;
import cn.imaq.lex.util.IDGen;
import cn.imaq.lex.util.LexException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class NFA {
    public static NFAState fromRE(String tag, List<RENode> reNodes, IDGen id) throws LexException {
        // Init
        int idOffset = id.get();
        Stack<Character> opStack = new Stack<>();
        Stack<NFAState> stateStack = new Stack<>();
        // Construct root
        List<NFAState> states = new ArrayList<>();
        NFAState root = new NFAState(id.next());
        states.add(root);
        // Parse RE
        NFAState lastBlockFrom = null;
        NFAState blockStart = root;
        NFAState ptr = root;
        for (int i = 0; i < reNodes.size(); i++) {
            RENode node = reNodes.get(i);
            if (node.type == RENode.Type.OP) {
                switch (node.ch) {
                    case '(':
                        opStack.push(node.ch);
                        stateStack.push(blockStart);
                        blockStart = ptr;
                        break;
                    case ')':
                        while (true) {
                            char op = opStack.pop();
                            if (op == '(') {
                                lastBlockFrom = blockStart;
                                blockStart = stateStack.pop();
                                break;
                            } else if (op == '|') {
                                stateStack.pop().next.add(null, ptr);
                            }
                        }
                        break;
                    case '|':
                        opStack.push(node.ch);
                        stateStack.push(ptr);
                        ptr = blockStart;
                        break;
                    case '*':
                        if (lastBlockFrom == null) {
                            throw new LexException("RE to NFA: \"*\" does not follow a block");
                        }
                        NFAState copyState = new NFAState(id.next());
                        states.add(copyState);
                        for (int j = idOffset; j < lastBlockFrom.id; j++) {
                            for (Character ch : states.get(j - idOffset).next.keySet()) {
                                if (states.get(j - idOffset).next.get(ch).contains(lastBlockFrom)) {
                                    states.get(j - idOffset).next.delete(ch, lastBlockFrom);
                                    states.get(j - idOffset).next.add(ch, copyState);
                                }
                            }
                        }
                        if (blockStart == lastBlockFrom) {
                            blockStart = copyState;
                        }
                        if (root == lastBlockFrom) {
                            root = copyState;
                        }
                        copyState.next.add(null, lastBlockFrom);
                        ptr.next.add(null, copyState);
                        // Add a null state
                        NFAState nullState = new NFAState(id.next());
                        states.add(nullState);
                        copyState.next.add(null, nullState);
                        ptr = nullState;
                        break;
                }
            } else {
                NFAState newState = new NFAState(id.next());
                states.add(newState);
                ptr.next.add(node.ch, newState);
                lastBlockFrom = ptr;
                ptr = newState;
            }
            // DEBUG
            if (Debug.debug) {
                for (int j = 0; j < states.size(); j++) {
                    System.out.print("NFAState[" + (j + idOffset) + "]: ");
                    for (Character nextChar : states.get(j).next.keySet()) {
                        System.out.print(nextChar + " -> " + Arrays.toString(states.get(j).next.get(nextChar).stream().map(s -> s.id).toArray()) + ", ");
                    }
                    System.out.println();
                }
                System.out.println();
            }
        }
        ptr.tag = tag;
        return root;
    }

    public static NFAState merge(List<NFAState> nfas, IDGen id) {
        if (nfas.size() == 1) {
            return nfas.get(0);
        }
        NFAState merged = new NFAState(id.next());
        for (NFAState nfa : nfas) {
            merged.next.add(null, nfa);
        }
        return merged;
    }
}
