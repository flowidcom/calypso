package com.flowid.xdo.util;

/**
 * Utility class that will allow the developer to assert conditions. Throw runtime exception if
 * assertion failed.
 */
public class AppAssert {
    static public void isTrue(boolean condition, String message) {
        if (!condition) {
            throw new AppException(ErrorUtils.FAIILED_ASSERTION, message);
        }
    }

    static public void isNotNull(Object o, String message) {
        isTrue(o != null, message);
    }

    public static void fail(String message) {
        throw new AppException(ErrorUtils.FAIILED_ASSERTION, message);
    }
}
