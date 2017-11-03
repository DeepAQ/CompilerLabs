package cn.imaq.lex.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Set;

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
            set = new LinkedHashSet<>();
            put(key, set);
        }
        set.add(value);
    }

    public void delete(K key, V value) {
        Set<V> set = get(key);
        if (!set.isEmpty()) {
            set.remove(value);
        }
    }
}
