package com.flowid.refd.config;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.flowid.refd.config.JacksonObjectMapper;

@Configuration
public class WebConfig extends WebMvcConfigurationSupport {
    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        logger.debug("Adding Jackson configuration.");
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(new JacksonObjectMapper());

        converters.add(converter);
    }

}
