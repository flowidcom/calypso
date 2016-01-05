package com.flowid.futils;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.flowid.xdo.cmn.StructMessage;

public class Result<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    T value = null;
    StructMessage error = null;

    public Result() {
    }

    public Result(T t) {
        this.value = t;
    }

    public Result(int code, String message) {
        error = new StructMessage().withCode(code).withMessage(message);
    }

    public T value() {
        return value;
    }

    public boolean isPresent() {
        return value != null;
    }

    public boolean notPresent() {
        return !isPresent();
    }

    public boolean isError() {
        return error != null;
    }

    public static <T> Result<T> value(T t) {
        return new Result<T>(t);
    }

    public static <T> Result<T> error(int code, String message) {
        return new Result<T>(code, message);
    }

    public Result<T> addParam(String name, String value) {
        error.getParams().add(ParamUtils.param(name, value));
        return this;
    }

    @Override
    public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE);
        if (value != null) {
            sb.append("value", value);
        }
        if (error != null) {
            sb.append("code", error.getCode())
                    .append("message", error.getMessage())
                    .append("params", ParamUtils.toString(error.getParams()));
        }
        return sb.toString();
    }

    public void setError(StructMessage err) {
        error = err;
    }
}
