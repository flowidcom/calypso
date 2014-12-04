package com.flowid.refd.domain;

public class ResourceNotFoundException extends GException {
    private static final long serialVersionUID = 1L;

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public int getHttpCode() {
        return 400;
    }
}
