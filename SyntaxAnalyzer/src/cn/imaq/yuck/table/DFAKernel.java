package cn.imaq.yuck.table;

import cn.imaq.yuck.cfg.Production;

public class DFAKernel {
    public Production production;

    public int pos;

    public String predict;

    public DFAKernel(Production production) {
        this(production, 0);
    }

    public DFAKernel(Production production, int pos) {
        this(production, pos, null);
    }

    public DFAKernel(Production production, int pos, String predict) {
        this.production = production;
        this.pos = pos;
        this.predict = predict;
    }

    public boolean isEnd() {
        return pos == production.right.size() || production.right.get(pos) == null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(production.left).append(" -> ");
        if (pos == 0) {
            sb.append('.');
        }
        for (int i = 0; i < production.right.size(); i++) {
            sb.append(production.right.get(i));
            if (i + 1 == pos) {
                sb.append('.');
            } else {
                sb.append(' ');
            }
        }
        sb.append("; ").append(predict == null ? "$" : predict).append(' ');
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DFAKernel kernel = (DFAKernel) o;

        if (pos != kernel.pos) return false;
        if (!production.equals(kernel.production)) return false;
        return predict != null ? predict.equals(kernel.predict) : kernel.predict == null;
    }

    @Override
    public int hashCode() {
        int result = production.hashCode();
        result = 31 * result + pos;
        result = 31 * result + (predict != null ? predict.hashCode() : 0);
        return result;
    }
}
