package cn.imaq.yuck.table.action;

import cn.imaq.yuck.cfg.Production;

public class ReduceAction implements IAction {
    public Production production;

    public ReduceAction(Production production) {
        this.production = production;
    }

    @Override
    public String toString() {
        return "r" + production.id;
    }
}
