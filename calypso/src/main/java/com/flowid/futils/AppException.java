package com.flowid.futils;

import java.util.ArrayList;
import java.util.List;

import com.flowid.xdo.cmn.Param;

public class AppException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    int code = 0;
    List<Param> params;

    public AppException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public AppException(String msg) {
        super(msg);
    }

    public AppException(int code, String msg, Throwable cause) {
        super(msg, cause);
        this.code = code;
    }

    public AppException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public AppException withCode(int code) {
        this.code = code;
        return this;
    }

    public int getCode() {
        return code;
    }

    public AppException addParam(String name, String value) {
        if (params == null) {
            params = new ArrayList<Param>();
        }
        params.add(ParamUtils.param(name, value));
        return this;
    }

    public List<Param> getParams() {
        return params;
    }

    /**
     * This wraps an existing exception, typically a checked exception to an AppException
     * @param t
     * @param string
     * @return
     */
    public static AppException wrap(Throwable t, String string) {
        if (t instanceof AppException) {
            // avoid wrapping the same exception multiple times
            return (AppException) t;
        }
        AppException e = new AppException(string, t);
        return e;
    }

    public AppException withParams(List<Param> params) {
        this.params = params;
        return this;
    }
}
