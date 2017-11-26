package cn.imaq.yuck.table;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DFAState {
    public int id;

    public Set<DFAKernel> kernel;

    public Set<DFAKernel> closure;

    public Map<String, DFAState> next;

    public DFAState(Set<DFAKernel> kernel) {
        this(-1, kernel);
    }

    public DFAState(int id, Set<DFAKernel> kernel) {
        this.id = id;
        this.kernel = kernel;
        this.closure = new HashSet<>();
        this.next = new HashMap<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("DFAState[").append(id).append("] closure={").append(closure).append("} next={");
        for (Map.Entry<String, DFAState> entry : next.entrySet()) {
            sb.append(entry.getKey()).append(" -> ").append(entry.getValue().id).append(", ");
        }
        sb.append("}\n");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DFAState state = (DFAState) o;
        return kernel.equals(state.kernel);
    }

    @Override
    public int hashCode() {
        return kernel.hashCode();
    }
}
