package com.flowid.refd.config;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

@SuppressWarnings("serial")
public class JacksonObjectMapper extends ObjectMapper {

    public JacksonObjectMapper() {
        this.enable(SerializationFeature.INDENT_OUTPUT);
        this.setSerializationInclusion(Include.NON_NULL);
        this.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        this.configure(SerializationFeature.WRITE_EMPTY_JSON_ARRAYS, false);
        this.registerModule(new JodaModule());
    }
}
