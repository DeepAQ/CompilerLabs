package cn.imaq.yuck.cfg;

import cn.imaq.yuck.util.IDGen;
import cn.imaq.yuck.util.MultiMap;
import cn.imaq.yuck.util.YuckException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CFG {
    public MultiMap<String, Production> productions;

    public String start;

    public MultiMap<String, String> first;

    public MultiMap<String, String> follow;

    public CFG(MultiMap<String, Production> productions, String start) {
        this.productions = productions;
        this.start = start;
        this.first = new MultiMap<>();
        this.follow = new MultiMap<>();
        calculateFirstFollow();
    }

    private void calculateFirstFollow() {
        // First
        while (true) {
            int lastSize = first.realSize();
            for (String nt : productions.keySet()) {
                for (Production prod : productions.get(nt)) {
                    for (int i = 0; i < prod.right.size(); i++) {
                        String symbol = prod.right.get(i);
                        if (!Objects.equals(symbol, nt)) {
                            if (productions.containsKey(symbol)) {
                                Set<String> target = first.get(symbol);
                                if (i == prod.right.size() - 1) {
                                    first.addAll(nt, target);
                                } else {
                                    first.addAll(nt, target.stream().filter(Objects::nonNull).collect(Collectors.toSet()));
                                }
                                if (!target.contains(null)) {
                                    break;
                                }
                            } else if (symbol != null || i == prod.right.size() - 1) {
                                first.add(nt, symbol);
                                break;
                            }
                        }
                    }
                }
            }
            if (first.realSize() <= lastSize) {
                break;
            }
        }
        // Follow
        follow.add(start, null);
        while (true) {
            int lastSize = follow.realSize();
            for (String nt : productions.keySet()) {
                for (Production prod : productions.get(nt)) {
                    for (int i = 0; i < prod.right.size(); i++) {
                        String symbol = prod.right.get(i);
                        if (productions.containsKey(symbol)) {
                            if (i == prod.right.size() - 1) {
                                follow.addAll(symbol, follow.get(nt));
                            } else {
                                String nextSymbol = prod.right.get(i + 1);
                                if (productions.containsKey(nextSymbol)) {
                                    follow.addAll(symbol, first.get(nextSymbol).stream().filter(Objects::nonNull).collect(Collectors.toSet()));
                                    if (first.get(nextSymbol).contains(null)) {
                                        follow.addAll(symbol, follow.get(nt));
                                    }
                                } else {
                                    follow.add(symbol, nextSymbol);
                                }
                            }
                        }
                    }
                }
            }
            if (follow.realSize() <= lastSize) {
                break;
            }
        }
    }

    public String firstFollowToString() {
        return "First: " + first + "\n" + "Follow: " + follow;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("CFG { Start: ").append(start).append(", Productions:\n");
        for (String left : productions.keySet()) {
            for (Production production : productions.get(left)) {
                sb.append("    ").append(production).append('\n');
            }
        }
        sb.append('}');
        return sb.toString();
    }

    public static CFG parse(List<String> prodStrs, String start) throws YuckException {
        IDGen id = new IDGen();
        MultiMap<String, Production> productions = new MultiMap<>();
        for (String prodStr : prodStrs) {
            String[] leftRight = prodStr.split("->", 2);
            if (leftRight.length != 2) {
                throw new YuckException("ParseCFG: \"" + prodStr + "\" is not a valid production");
            }
            String left = leftRight[0].trim();
            List<String> right = Arrays.stream(leftRight[1].trim().split(" "))
                    .filter(s -> !s.isEmpty())
                    .map(s -> s.equals("''") ? null : s)
                    .collect(Collectors.toList());
            productions.add(left, new Production(id.next(), left, right));
        }
        return new CFG(productions, start);
    }
}
