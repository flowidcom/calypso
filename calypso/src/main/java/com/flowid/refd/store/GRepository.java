package com.flowid.refd.store;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.apache.commons.lang.ObjectUtils;

public class GRepository<T, K extends Serializable> {
    List<T> items = Collections.synchronizedList(new ArrayList<T>());

    public List<T> getAll() {
        return items;
    }

    public T find(K k) {
        for (T item : items) {
            if (ObjectUtils.equals(k, index(item))) {
                return item;
            }
        }
        return null;
    }

    public T save(T t) {
        K k = index(t);
        T result = null;
        for (ListIterator<T> it = items.listIterator(); it.hasNext();) {
            T item = it.next();
            if (ObjectUtils.equals(k, index(item))) {
                result = item;
                it.remove();
                it.add(t);
            }
        }
        if (result == null) {
            result = t;
            items.add(t);
        }
        return result;
    }

    public void deleteAll() {
        items.clear();
    }

    public T delete(K k) {
        for (Iterator<T> it = items.iterator(); it.hasNext();) {
            T item = it.next();
            if (ObjectUtils.equals(k, index(item))) {
                it.remove();
                return item;
            }
        }
        return null;
    }

    protected K index(T t) {
        return null;
    }
}
