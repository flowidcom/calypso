package com.flowid.xdo.util;

import javax.xml.ws.WebServiceException;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import com.flowid.xdo.cmn.Param;
import com.flowid.xdo.cmn.StructMessage;


/**
 * Base class for errors. Also defines a few errors common across components.
 */
public class ErrorUtils {
    private static final int STACK_TRACE_MAX_SIZE = 500;

    public static final int UNKNOWN_EXCEPTION = 1000;
    public static final int NOT_IMPLEMENTED = 1001;
    public static final int FAIILED_ASSERTION = 1002;
    public static final int CONNECTION_ERROR = 1003;
    public static final int CICS_INVOCATION_ERROR = 1004;
    public static final int JAXB_EXCEPTION = 1005;

    public static final int FILE_TOKENIZER_ALL_ERRORS = 2001;


    public static void addExceptionDetails(StructMessage err, Throwable t) {
        addParam(err, "exceptionClass", t.getClass().getName());
        addParam(err, "exceptionMessage", t.getMessage());
        addParam(err, "stackTrace", getStackTrace(t));
        Throwable c = t.getCause();
        int i = 0;
        while (c != null && i < 4) {
            addParam(err, "exceptionCause" + i, c.getClass().getSimpleName() + " - " + c.getMessage());
            i++;
        }
    }

    public static String getStackTrace(Throwable t) {
        return StringUtils.abbreviate(ExceptionUtils.getStackTrace(t), STACK_TRACE_MAX_SIZE);
    }

    public static void addParam(StructMessage err, String name, Object o) {
        String value = "";
        if (o == null) {
            value = "null";
        }
        else if (o instanceof Exception) {
            value = ((Exception) o).getMessage();
        }
        else {
            value = o.toString();
        }
        err.getParams().add(new Param()
                .withName(name)
                .withValue(value));
    }

    public static Throwable getRootCause(Throwable e) {
        Throwable root = e;
        while (root.getCause() != null) {
            root = root.getCause();
        }
        return root;
    }

    public static String toString(AppException e) {
        StringBuilder sb = new StringBuilder();
        sb.append(e.getCode()).append('-');
        sb.append(e.getMessage());

        if (e.getParams() != null) {
            sb.append(':');
            boolean isFirst = true;
            for (Param p : e.getParams()) {
                if (!isFirst) {
                    sb.append(',');
                }
                sb.append(p.getName()).append(":").append(p.getValue());
                isFirst = false;
            }
        }
        return sb.toString();
    }

    static public StructMessage convertExceptionToError(Throwable e) {
        StructMessage err = null;
        if (e instanceof AppException) {
            AppException ae = (AppException) e;
            err = codemsg(ae.getCode(), ae.getMessage());
            if (ae.getParams() != null) {
                err.getParams().addAll(ae.getParams());
            }
            if (ae.getCause() != null) {
                addExceptionDetails(err, ae.getCause());
            }
        }
        else if (e instanceof WebServiceException) {
            err = codemsg(CONNECTION_ERROR, e.getCause().getMessage());
            addExceptionDetails(err, e);
        }
        else {
            err = codemsg(UNKNOWN_EXCEPTION, e.getMessage());
            addExceptionDetails(err, e);
        }
        return err;
    }

    static public StructMessage error(int code, String message, String... params) {
        return codemsg(code, message, params);
    }

    static public StructMessage warning(int code, String message, String... params) {
        return codemsg(code, message, params);
    }

    static public StructMessage codemsg(int code, String message, String... params) {
        StructMessage m = new StructMessage();
        m.setCode(code);
        m.setMessage(message);
        for (int i = 0; i < params.length / 2; i++) {
            Param p = new Param()
                    .withName(params[2 * i])
                    .withValue(params[2 * i + 1]);
            m.getParams().add(p);
        }
        return m;
    }

    public static <T> void setResultErrorFromException(Result<T> result, Throwable e) {
        StructMessage err = convertExceptionToError(e);
        result.setError(err);
    }

}
