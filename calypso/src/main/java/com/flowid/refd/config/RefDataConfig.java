package com.flowid.refd.config;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import com.fasterxml.jackson.jaxrs.cfg.Annotations;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

@Configuration
public class RefDataConfig {
    static private final String APP_NAME = "calypso";

    @Bean
    static public PropertyPlaceholderConfigurer readAllProperties() {
        PropertyPlaceholderConfigurer ppc = new PropertyPlaceholderConfigurer();
        ppc.setLocations(new Resource[] {
                new FileSystemResource(appPropertiesUrl()),
                new FileSystemResource(etcPropertiesUrl())
        });
        ppc.setIgnoreResourceNotFound(true);
        ppc.setIgnoreUnresolvablePlaceholders(true);
        return ppc;
    }

    @Bean(name = "appProps")
    static public PropertiesFactoryBean getPropertieFactoryBean() {
        PropertiesFactoryBean props = new PropertiesFactoryBean();
        props.setLocations(new Resource[] {
                new FileSystemResource(appPropertiesUrl())
        });
        props.setIgnoreResourceNotFound(true);
        props.setIgnoreResourceNotFound(true);
        return props;
    }

    static public String appPropertiesUrl() {
        String appConfigDir = System.getProperty("app.config.dir");
        return String.format("%s/%s-app.properties", appConfigDir, APP_NAME);
    }

    static private String etcPropertiesUrl() {
        String appEnv = System.getProperty("app.env");
        String userHome = System.getProperty("user.home");
        return String.format("%s/.apps/%s-etc-%s.properties", userHome, APP_NAME, appEnv);

    }

    @Bean(name = "jsonProvider")
    public JacksonJsonProvider getJsonProvider() {
        return new JacksonJaxbJsonProvider(new JacksonObjectMapper(), new Annotations[0]);
    }
}
