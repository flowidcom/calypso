package com.flowid.xdo.util;

import java.util.ArrayList;
import java.util.List;

import com.flowid.xdo.cmn.Param;

/**
 * Generic exception thrown by CRM components.
 */
public class AppException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    int code;
    List<Param> params = new ArrayList<Param>();

    /**
     * @see Exception#Exception(String)
     */
    public AppException(int errorCode, String errorMsg) {
        super(errorMsg);
    }

    public AppException(int errorCd, String errorMsg, Throwable t) {
        super(errorMsg, t);
    }

    public AppException addParam(String name, String value) {
        params.add(new Param().withName(name).withValue(value));
        return this;
    }

    public int getCode() {
        return code;
    }

    public List<Param> getParams() {
        return params;
    }
}
