package cn.imaq.yuck.table;

import cn.imaq.yuck.table.action.IAction;

import java.util.HashMap;
import java.util.Map;

public class TableState {
    public int id;

    public Map<String, IAction> actions;

    public TableState(int id) {
        this.id = id;
        this.actions = new HashMap<>();
    }

    @Override
    public String toString() {
        return "TableState[" + id + "] actions=" + actions + "\n";
    }
}
