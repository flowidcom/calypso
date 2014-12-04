package com.flowid.refd.service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.flowid.refd.domain.BadRequestException;
import com.flowid.refd.domain.ResourceNotFoundException;
import com.flowid.refd.store.GRepository;
import com.flowid.refd.v1.Country;
import com.flowid.refd.v1.GList;
import com.flowid.refd.v1.Upload;

@Path("/countries")
public class CountryResource {
    private static final Logger logger = LoggerFactory.getLogger(CountryResource.class);

    static private GRepository<Country, String> repository = new GRepository<Country, String>() {
        public String index(Country c) {
            return c.getCode();
        }
    };

    static {
        repository.save(new Country().withCode("CA").withName("Canada"));
        repository.save(new Country().withCode("US").withName("United States"));
    }

    @GET
    @Produces("application/json")
    public GList getCountries() {
        Object[] lo = repository.getAll().toArray();
        GList pl = new GList()
            .withStart(1)
            .withTotalCount(lo.length);
        pl.getItems().addAll(Arrays.asList(lo));
        return pl;
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Country postCountry(Country country) {
        return repository.save(country);
    }

    @GET
    @Produces("application/json")
    @Path("/{cd}")
    public Country getCountryByCd(@PathParam("cd") String cd) {
        return repository.find(cd);
    }

    @DELETE
    @Produces("application/json")
    @Path("/{cd}")
    public Country removeCountryByCd(@PathParam("cd") String cd) {
        return repository.delete(cd);
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/{cd}")
    public Country updateCountry(@PathParam("cd") String cd, Country country) {
        if (cd == null || !cd.equals(country.getCode())) {
            throw new BadRequestException("Invalid key");
        }
        Country c = repository.find(cd);
        if (c == null) {
            throw new ResourceNotFoundException("Incorrect");
        }
        return repository.save(country);
    }

    @POST
    @Path("/action/upload")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Upload uploadCountries(MultipartBody body) throws Exception {
        try {
            Attachment attachment = body.getAllAttachments().get(0);
            logger.debug("Loading the file: {}/{}", attachment.getContentId(), attachment.getContentType());
            ContentDisposition cd = attachment.getContentDisposition();
            logger.debug("Content disposition: {}", cd.getType());
            repository.deleteAll();
            InputStream is = attachment.getDataHandler().getInputStream();
            Reader r = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(r);
            String line = null;
            boolean isFirst = true;
            while ((line = br.readLine()) != null) {
                // skip the first line
                if (isFirst) {
                    isFirst = false;
                    continue;
                }
                String[] parts = line.split(",");
                Country c = new Country()
                    .withCode(parts[10])
                    .withName(parts[1]);
                repository.save(c);
            }
            IOUtils.closeQuietly(is);
        } catch (Exception e) {
            logger.error("While ... {}", e.getMessage());
            logger.debug("Details: ", e);
            throw e;
        }
        Upload u = new Upload();
        u.setTemplateId(1234L);
        return u;
    }
}
