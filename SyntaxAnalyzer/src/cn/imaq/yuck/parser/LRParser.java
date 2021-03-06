package cn.imaq.yuck.parser;

import cn.imaq.yuck.cfg.CFG;
import cn.imaq.yuck.cfg.Production;
import cn.imaq.yuck.table.DFAKernel;
import cn.imaq.yuck.table.DFAState;
import cn.imaq.yuck.table.TableState;
import cn.imaq.yuck.table.action.ReduceAction;
import cn.imaq.yuck.table.action.ShiftAction;
import cn.imaq.yuck.util.IDGen;
import cn.imaq.yuck.util.MultiMap;
import cn.imaq.yuck.util.YuckException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SuppressWarnings("Duplicates")
public class LRParser implements IParser {
    @Override
    public List<TableState> toParseTable(CFG cfg) throws YuckException {
        IDGen id = new IDGen();
        List<TableState> table = new ArrayList<>();
        List<DFAState> states = new ArrayList<>();
        Set<DFAKernel> startKernel = cfg.productions.get(cfg.start).stream().map(p -> new DFAKernel(p, 0)).collect(Collectors.toSet());
        states.add(new DFAState(id.next(), startKernel));
        for (int i = 0; i < states.size(); i++) {
            DFAState state = states.get(i);
            // Calculate closure
            for (DFAKernel kernel : state.kernel) {
                addClosure(state.closure, kernel, cfg);
            }
            // Calculate moves
            MultiMap<String, DFAKernel> moves = new MultiMap<>();
            for (DFAKernel kernel : state.closure) {
                if (!kernel.isEnd()) {
                    moves.add(kernel.production.right.get(kernel.pos), new DFAKernel(kernel.production, kernel.pos + 1, kernel.predict));
                }
            }
            for (String move : moves.keySet()) {
                DFAState nextState = new DFAState(moves.get(move));
                int index = states.indexOf(nextState);
                if (index >= 0) {
                    nextState = states.get(index);
                } else {
                    nextState.id = id.next();
                    states.add(nextState);
                }
                state.next.put(move, nextState);
            }
            // Add state to table
            TableState tableState = new TableState(i);
            for (DFAKernel item : state.closure) {
                if (item.isEnd()) {
                    if (tableState.actions.containsKey(item.predict) && !(tableState.actions.get(item.predict) instanceof ReduceAction)) {
                        throw new YuckException("LR parsing table conflict: state=" + i + ", next=" + item.predict);
                    }
                    tableState.actions.put(item.predict, new ReduceAction(item.production));
                } else {
                    String symbol = item.production.right.get(item.pos);
                    if (tableState.actions.containsKey(symbol) && !(tableState.actions.get(symbol) instanceof ShiftAction)) {
                        throw new YuckException("LR parsing table conflict: state=" + i + ", next=" + item.predict);
                    }
                    tableState.actions.put(symbol, new ShiftAction(state.next.get(symbol).id));
                }
            }
            table.add(tableState);
        }
        return table;
    }

    private void addClosure(Set<DFAKernel> closure, DFAKernel item, CFG cfg) {
        if (closure.contains(item)) {
            return;
        }
        closure.add(item);
        if (item.pos < item.production.right.size()) {
            Set<Production> next = cfg.productions.get(item.production.right.get(item.pos));
            if (!next.isEmpty()) {
                String firstSymbol = item.predict;
                if (item.pos + 1 < item.production.right.size()) {
                    firstSymbol = item.production.right.get(item.pos + 1);
                }
                Set<String> firstTerm;
                if (cfg.productions.containsKey(firstSymbol)) {
                    firstTerm = cfg.first.get(firstSymbol);
                } else {
                    firstTerm = Collections.singleton(firstSymbol);
                }
                for (Production production : next) {
                    for (String first : firstTerm) {
                        addClosure(closure, new DFAKernel(production, 0, first), cfg);
                    }
                }
            }
        }
    }
}
