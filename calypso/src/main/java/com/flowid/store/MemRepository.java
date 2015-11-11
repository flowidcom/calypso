package com.flowid.store;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.flowid.xdo.util.AppAssert;

/**
 * Basic implementation of an in-memory repository used in testing collection-based resources.
 */
public class MemRepository<K extends Serializable, T> implements GRepository<K, T> {
    Map<K, T> map = new ConcurrentHashMap<>();

    public Collection<T> getAll() {
        return map.values();
    }

    @Override
    public T find(K k) {
        return map.get(k);
    }

    @Override
    public T save(T t) {
        K k = key(t);
        T result = map.remove(k);
        map.put(k, t);
        return result;
    }

    public void deleteAll() {
        map.clear();
    }

    @Override
    public T delete(K k) {
        return map.remove(k);
    }

    @Override
    public K key(T t) {
        AppAssert.fail("Operation must be overwritten in derived class");
        return null;
    }
}
