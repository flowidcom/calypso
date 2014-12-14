package com.flowid.refd.store;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Very simple an in-memory repository used in testing collection-based resources. In real
 * applications this would be an interface with several implementations
 */
public class GRepository<T, K extends Serializable> {
    Map<K, T> map = new ConcurrentHashMap<>();

    public Collection<T> getAll() {
        return map.values();
    }

    public T find(K k) {
        return map.get(k);
    }

    public T save(T t) {
        K k = index(t);
        T result = map.remove(k);
        map.put(k, t);
        return result;
    }

    public void deleteAll() {
        map.clear();
    }

    public T delete(K k) {
        return map.remove(k);
    }

    protected K index(T t) {
        return null;
    }
}
