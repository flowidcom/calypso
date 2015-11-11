package com.flowid.test.rest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import junit.framework.Assert;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.flowid.rest.test.AbstractJIntTest;
import com.flowid.xdo.cmn.GList;
import com.flowid.xdo.cmn.Upload;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/spring/calypso.context.xml"})
public class JIntTestCountries extends AbstractJIntTest {
    private static final Logger logger = LoggerFactory.getLogger(JIntTestCountries.class);

    @BeforeClass
    public static void setUp() {
        AbstractJIntTest.setEnv(ENV_LOCAL);
    }

    @Value("${calypso.url}")
    String appBaseUrl;

    @Autowired
    JacksonJsonProvider jsonProvider;

    @Test
    public void testGetCountries() throws Exception {
        WebClient wc = WebClient
            .create(appBaseUrl + "/rs/countries", Arrays.asList(jsonProvider))
            .type(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

        ClientConfiguration config = WebClient.getConfig(wc);
        config.getOutInterceptors().add(new LoggingOutInterceptor());
        config.getInInterceptors().add(new LoggingInInterceptor());
        GList response = wc.get(GList.class);
        logger.debug("Response: - {}", response);
        Assert.assertEquals(2, response.getItems().size());
    }

    @Test
    public void testPostCountries() throws FileNotFoundException {
        WebClient wc = WebClient.create(appBaseUrl + "/rs/countries" + "/action/upload", Arrays.asList(jsonProvider))
            .type(MediaType.MULTIPART_FORM_DATA)
            .accept(MediaType.APPLICATION_JSON);

        ClientConfiguration config = WebClient.getConfig(wc);
        config.getOutInterceptors().add(new LoggingOutInterceptor());
        config.getInInterceptors().add(new LoggingInInterceptor());

        InputStream is = new FileInputStream("src/test/data/in/country-codes-1.csv");

        Upload u = new Upload();
        u.setTemplateId(1234L);

        ContentDisposition cd = new ContentDisposition("attachment;filename=country-codes-1.csv");
        Attachment att = new Attachment("root", is, cd);

        Response response = wc.post(new MultipartBody(att));
        IOUtils.closeQuietly(is);

        Assert.assertEquals(200, response.getStatus());
    }
}
