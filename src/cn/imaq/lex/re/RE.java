package cn.imaq.lex.re;

import java.util.ArrayList;
import java.util.List;

public class RE {
    public static List<RENode> parse(String re) {
        List<RENode> result = new ArrayList<>();
        result.add(new RENode(RENode.Type.OP, '('));
        for (int i = 0; i < re.length(); i++) {
            char c = re.charAt(i);
            switch (c) {
                case '(':
                case ')':
                case '|':
                case '*':
                    result.add(new RENode(RENode.Type.OP, c));
                    break;
                default:
                    result.add(new RENode(RENode.Type.CH, c));
            }
        }
        result.add(new RENode(RENode.Type.OP, ')'));
        return result;
    }
}
