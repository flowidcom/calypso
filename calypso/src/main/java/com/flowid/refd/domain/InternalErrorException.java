package com.flowid.refd.domain;


public class InternalErrorException extends GException {
    private static final long serialVersionUID = 1L;

    public InternalErrorException() {
        super();
    }

    public InternalErrorException(String message) {
        super(message);
    }

    public int getHttpCode() {
        return 500;
    }
}
