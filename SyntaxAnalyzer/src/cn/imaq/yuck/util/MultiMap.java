package cn.imaq.yuck.util;

import java.util.*;

public class MultiMap<K, V> extends HashMap<K, Set<V>> {
    @Override
    public Set<V> get(Object key) {
        Set<V> set = super.get(key);
        if (set == null) {
            return Collections.emptySet();
        } else {
            return set;
        }
    }

    public void add(K key, V value) {
        Set<V> set = get(key);
        if (set.isEmpty()) {
            set = new HashSet<>();
            put(key, set);
        }
        set.add(value);
    }

    public void addAll(K key, Collection<? extends V> values) {
        Set<V> set = get(key);
        if (set.isEmpty()) {
            set = new HashSet<>();
            put(key, set);
        }
        set.addAll(values);
    }

    public void delete(K key, V value) {
        Set<V> set = get(key);
        if (!set.isEmpty()) {
            set.remove(value);
        }
    }

    public int realSize() {
        return values().stream().mapToInt(Set::size).sum();
    }
}
