package com.flowid.refd.domain;

import com.flowid.refd.v1.GError;

/**
 * This is a generic exception returned by the application.
 */
public class GException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private int code = 1000;

    public int getCode() {
        return code;
    }

    public GException withCode(int code) {
        this.code = code;
        return this;
    }

    public GException() {
        super();
    }

    public GException(String message) {
        super(message);
    }

    public GException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getHttpCode() {
        return 500;
    }

    public GError asError() {
        return new GError().withCode(code).withMessage(getMessage());
    }
}
