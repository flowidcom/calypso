package com.flowid.futils;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Helper class used to deal Java hierarchy of objects where there can be nulls at various steps.
 */
public class NullSafe {
    private static final Logger logger = LoggerFactory.getLogger(NullSafe.class);

    // this is a cache of null objects. For each class there is one null object
    static Map<String, Object> nullObjects = new ConcurrentHashMap<String, Object>();

    static <T> T getNullObject(Class<T> c) {
        AppAssert.isNotNull(c, "Cannot create an instance for an empty object");
        @SuppressWarnings("unchecked")
        T o = (T) nullObjects.get(c.getName());
        if (o == null) {
            o = NullSafe.createNullObjectUsingConstructor(c);
        }
        return o;
    }

    static <T> T createNullObjectUsingConstructor(Class<T> c) {
        try {
            Constructor<T> con = c.getConstructor();
            T o = con.newInstance();
            return o;
        }
        catch (Exception e) {
            logger.error("While creating and instance of " + c.getCanonicalName() + " - " + e.getMessage());
            logger.debug("Details:", e);
        }
        return null;
    }

    static public <T> boolean isNotNull(T o) {
        return o != null;
    }

    static public <T> boolean isNull(T o) {
        return o == null;
    }

    static public <T> T nullSafe(T o, T d) {
        if (o == null) {
            return d;
        }
        return o;
    }

    static public <T> List<T> nullSafe(T[] o) {
        if (o == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(o);
    }

    static public <T> T nullSafe(T o, Class<T> clasz) {
        if (o == null) {
            return NullSafe.getNullObject(clasz);
        }
        return o;
    }

    public static String toString(Object o) {
        if (o == null) {
            return null;
        }
        return o.toString();
    }

}
