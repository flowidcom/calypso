package com.flowid.futils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flowid.xdo.cmn.Param;

public class ParamUtils {
    private static final Logger logger = LoggerFactory.getLogger(ParamUtils.class);

    // The following are regular name characters. They appear in option names and property names
    private static final String NAME_CHAR = "[a-z]|[A-Z]|[0-9]|_|\\.|\\-";
    // Values of the properties can be any character
    private static final String VALUE_CHAR = ".";
    private static final String PROP_DEF_REGEX = "((" + NAME_CHAR + ")+)=((" + VALUE_CHAR + ")+)";
    private static final Pattern PROP_DEF_PATTERN = Pattern.compile(PROP_DEF_REGEX);

    static public Param param(String name, Object value) {
        return new Param().withName(name).withValue(value == null ? null : value.toString());
    }

    static public Param param(String value) {
        return param(null, value);
    }

    /**
     * Utility function to break a string of name=value in a map
     */
    static public Map<String, String> splitNameValuePairs(String s, String separator) {
        Map<String, String> props = new LinkedHashMap<>();
        String[] parts = s.split(separator);
        for (String token : parts) {
            Matcher propDefMatcher = PROP_DEF_PATTERN.matcher(token);
            if (propDefMatcher.matches()) {
                String propName = propDefMatcher.group(1);
                String propValue = propDefMatcher.group(3);
                props.put(propName, propValue);
            }
            else {
                logger.error("Invalid NVP {}", token);
            }
        }
        return props;
    }

    public static String toString(List<Param> params) {
        StringBuilder sb = new StringBuilder();
        if (params != null) {
            boolean isFirst = true;
            for (Param p : params) {
                if (!isFirst) {
                    sb.append(',');
                }
                sb.append(p.getName()).append(":").append(p.getValue());
                isFirst = false;
            }
        }
        return sb.toString();
    }

    public static String getParam(List<Param> paramList, String name) {
        for (Param param : paramList) {
            if (name != null && name.equals(param.getName())) {
                return param.getValue();
            }
        }
        return null;
    }

    public static void addIfNotNull(List<Param> params, String name, String value) {
        if (value != null) {
            params.add(param(name, value));
        }
    }

    public static void addIfNotNull(List<Param> params, String name, String value, String defaultValue) {
        String v = value == null ? defaultValue : value;
        addIfNotNull(params, name, v);
    }

    static public String mask(String s, int start, int length) {
        int adjstart = Math.min(start, s.length());
        int adjend = Math.min(start + length, s.length());
        int adjlen = adjend - adjstart;
        StringBuilder sb = new StringBuilder();
        sb.append(s.substring(0, adjstart));
        sb.append(StringUtils.repeat("*", adjlen));
        sb.append(s.substring(adjstart + adjlen, s.length()));
        return sb.toString();
    }
}
