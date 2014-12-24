package com.flowid.shell;

/**
 * This class holds a reference to another object. Used to implement passing by reference to the
 * ParamValidator methods.
 */
public class Ref<T> {
    T ref;

    public T get() {
        return ref;
    }

    public void set(T o) {
        ref = o;
    }
}
