package com.flowid.refd.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.annotation.ResponseStatusExceptionResolver;

@Configuration
public class DataUploadWsConfig {
    private static final Logger logger = LoggerFactory.getLogger(DataUploadWsConfig.class);

    @Bean
    public HandlerExceptionResolver responseStatusExceptionResolver() {
        logger.debug("Initializing exception resolver");
        ResponseStatusExceptionResolver resolver = new ResponseStatusExceptionResolver();
        return resolver;
    }
}
