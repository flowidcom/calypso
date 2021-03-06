package com.flowid.refd.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.feature.Feature;
import org.apache.cxf.interceptor.Interceptor;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.model.wadl.WadlGenerator;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.apache.cxf.message.Message;
import org.apache.cxf.rs.security.cors.CrossOriginResourceSharingFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import com.fasterxml.jackson.jaxrs.cfg.Annotations;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.flowid.futils.AppException;
import com.flowid.refd.service.CountryResource;

@Configuration
public class CxfConfig {
    private static final Logger logger = LoggerFactory.getLogger(CxfConfig.class);

    @Autowired
    JacksonJsonProvider jsonProvider;

    @Bean(destroyMethod = "shutdown")
    public SpringBus cxf() {
        SpringBus cxf = new SpringBus();
        LoggingInInterceptor loggingInInterceptor = new LoggingInInterceptor();
        loggingInInterceptor.setPrettyLogging(true);
        LoggingOutInterceptor loggingOutInterceptor = new LoggingOutInterceptor();
        loggingOutInterceptor.setPrettyLogging(true);
        cxf.setInInterceptors(
                Arrays.<Interceptor<? extends Message>> asList(
                        loggingInInterceptor));
        cxf.setOutInterceptors(
                Arrays.<Interceptor<? extends Message>> asList(
                        loggingOutInterceptor));
        cxf.setInFaultInterceptors(Arrays.<Interceptor<? extends Message>> asList(
                loggingInInterceptor));
        cxf.setOutFaultInterceptors(Arrays.<Interceptor<? extends Message>> asList(
                loggingOutInterceptor));
        cxf.setProperty("bus.jmx.enabled", "true");
        cxf.setProperty("faultStackTraceEnabled", "true");
        cxf.setProperty("exceptionMessageCauseEnabled", "true");
        cxf.setProperty("schema-validation-enabled", "false");

        return cxf;
    }

    @Bean
    LoggingInInterceptor createLoggingInInterceptor() {
        LoggingInInterceptor l = new LoggingInInterceptor();
        l.setPrettyLogging(true);
        return l;
    }

    @Bean
    LoggingOutInterceptor createLoggingOutInterceptor() {
        LoggingOutInterceptor l = new LoggingOutInterceptor();
        l.setPrettyLogging(true);
        return l;
    }

    @Bean
    @DependsOn({ "cxf", "jsonProvider" })
    public Server jaxRsServer() {
        final JAXRSServerFactoryBean factory = RuntimeDelegate.getInstance().createEndpoint(
                new Application() {
                    @Override
                    public Set<Class<?>> getClasses() {
                        HashSet<Class<?>> classes = new HashSet<Class<?>>();
                        classes.add(CountryResource.class);
                        return classes;
                    }
                },
                JAXRSServerFactoryBean.class);
        factory.setServiceBeans(Arrays.<Object> asList(country2Resource()));
        factory.setAddress("/");

        ExceptionMapper<AppException> gExceptionHandler = new ExceptionMapper<AppException>() {
            @Override
            public Response toResponse(AppException ge) {
                logger.error("Exception - {}", ge.getMessage());
                logger.debug("Details: ", ge);
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ge.getMessage()).build();
            }
        };

        ExceptionMapper<Exception> exceptionHandler = new ExceptionMapper<Exception>() {
            @Override
            public Response toResponse(Exception e) {
                logger.error("Exception - {}", e.getMessage());
                logger.debug("Details: ", e);
                return Response.status(500).entity(e.getMessage()).build();
            }
        };

        factory.setProviders(
                Arrays.asList(
                        jsonProvider,
                        getWadlGenerator(),
                        gExceptionHandler,
                        exceptionHandler,
                        new CrossOriginResourceSharingFilter()));
        ArrayList<Feature> features = new ArrayList<Feature>();
        features.addAll(cxf().getFeatures());
        features.add(getSwagger2Feature());
        factory.setFeatures(features);

        return factory.create();
    }

    @Bean
    CountryResource country2Resource() {
        return new CountryResource();
    }

    @Bean(name = "wadlGenerator")
    public WadlGenerator getWadlGenerator() {
        WadlGenerator wg = new WadlGenerator();
        wg.setApplicationTitle("Reference Data Management");
        wg.setNamespacePrefix("rd");
        wg.setLinkAnyMediaTypeToXmlSchema(true);
        wg.setAddResourceAndMethodIds(false);
        return wg;
    }

    @Bean
    public Swagger2Feature getSwagger2Feature() {
        Swagger2Feature sw = new Swagger2Feature();
        sw.setBasePath("/calypso-ws");
        sw.setTitle("Countries Service");
        sw.setDescription("Information about countries.");
        sw.setResourcePackage(CountryResource.class.getPackage().getName());
        sw.setContact(null);
        sw.setScan(true);

        return sw;
    }

    public JacksonJsonProvider getJsonProvider() {
        return new JacksonJaxbJsonProvider(new JacksonObjectMapper(), new Annotations[0]);
    }
}
