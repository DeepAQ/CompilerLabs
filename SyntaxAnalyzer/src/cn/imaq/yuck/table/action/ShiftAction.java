package cn.imaq.yuck.table.action;

public class ShiftAction implements IAction {
    public int next;

    public ShiftAction(int next) {
        this.next = next;
    }

    @Override
    public String toString() {
        return "S" + next;
    }
}
