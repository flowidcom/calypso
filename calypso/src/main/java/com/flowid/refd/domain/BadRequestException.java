package com.flowid.refd.domain;

public class BadRequestException extends GException {
    private static final long serialVersionUID = 1L;

    public BadRequestException(String msg) {
        super(msg);
    }
}
