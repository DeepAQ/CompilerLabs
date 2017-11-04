package cn.imaq.lex.re;

import cn.imaq.lex.util.LexException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RE {
    public static List<RENode> parse(String re, Map<String, List<RENode>> reMap) throws LexException {
        List<RENode> result = new ArrayList<>();
        result.add(new RENode(RENode.Type.OP, '('));
        boolean escape = false;
        for (int i = 0; i < re.length(); i++) {
            char c = re.charAt(i);
            if (escape) {
                escape = false;
                if (c == 'n') {
                    result.add(new RENode(RENode.Type.CH, '\n'));
                } else {
                    result.add(new RENode(RENode.Type.CH, c));
                }
                continue;
            }
            switch (c) {
                case '(':
                case ')':
                case '|':
                case '*':
                case '?':
                    result.add(new RENode(RENode.Type.OP, c));
                    break;
                case '[':
                    result.add(new RENode(RENode.Type.OP, '('));
                    i++;
                    while (re.charAt(i) != ']') {
                        char start = re.charAt(i);
                        i++;
                        if (re.charAt(i) != '-') {
                            throw new LexException("RE Parsing: Illegal character class at pos " + i);
                        }
                        i++;
                        char end = re.charAt(i);
                        for (char ch = start; ch <= end; ch++) {
                            result.add(new RENode(RENode.Type.CH, ch));
                            result.add(new RENode(RENode.Type.OP, '|'));
                        }
                        i++;
                    }
                    result.remove(result.size() - 1);
                    result.add(new RENode(RENode.Type.OP, ')'));
                    break;
                case '{':
                    int index = re.indexOf('}', i);
                    if (index < 0 || re.charAt(index - 1) == '\\') {
                        throw new LexException("RE Parsing: Illegal reference at pos " + i);
                    }
                    String refName = re.substring(i + 1, index);
                    if (reMap != null && reMap.containsKey(refName)) {
                        result.addAll(reMap.get(refName));
                        i = index;
                    } else {
                        throw new LexException("RE Parsing: Can not find reference \"" + refName + "\" at pos " + i);
                    }
                    break;
                case '+':
                    RENode last = result.get(result.size() - 1);
                    if (last.type == RENode.Type.CH) {
                        result.add(last);
                        result.add(new RENode(RENode.Type.OP, '*'));
                    } else if (last.ch == ')') {
                        int par = 1;
                        for (int j = result.size() - 2; j >= 0; j--) {
                            if (result.get(j).type == RENode.Type.OP) {
                                if (result.get(j).ch == ')') {
                                    par++;
                                } else if (result.get(j).ch == '(') {
                                    par--;
                                }
                                if (par == 0) {
                                    result.addAll(result.subList(j, result.size()));
                                    result.add(new RENode(RENode.Type.OP, '*'));
                                    break;
                                }
                            }
                        }
                    } else {
                        throw new LexException("RE Parsing: Illegal usage of \"+\" at pos " + i);
                    }
                    break;
                case '\\':
                    escape = true;
                    break;
                case '.':
                    result.add(new RENode(RENode.Type.CH, (char) 0));
                    break;
                default:
                    result.add(new RENode(RENode.Type.CH, c));
            }
        }
        result.add(new RENode(RENode.Type.OP, ')'));
        return result;
    }

    public static List<RENode> parse(String re) throws LexException {
        return parse(re, null);
    }
}
