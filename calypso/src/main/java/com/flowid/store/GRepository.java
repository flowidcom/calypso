package com.flowid.store;

import java.io.Serializable;

public interface GRepository<K extends Serializable, T> {

    T delete(K k);

    T save(T t);

    T find(K k);

    K key(T t);

}