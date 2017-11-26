package cn.imaq.yuck.cfg;

import java.util.List;
import java.util.stream.Collectors;

public class Production {
    public int id;

    public String left;

    public List<String> right;

    public Production(int id, String left, List<String> right) {
        this.id = id;
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        return left + " -> " + right.stream().collect(Collectors.joining(" "));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Production that = (Production) o;
        return left.equals(that.left) && right.equals(that.right);
    }

    @Override
    public int hashCode() {
        int result = left.hashCode();
        result = 31 * result + right.hashCode();
        return result;
    }
}
