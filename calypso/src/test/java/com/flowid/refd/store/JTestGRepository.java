package com.flowid.refd.store;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/calypso.context.xml"})
public class JTestGRepository {

    @Test
    public void testRepository() {
        GRepository<String, String> repository = new GRepository<String, String>() {
            protected String index(String s) {
                return s;
            }
        };

        repository.save("a");
        String s = repository.find("a");
        assertEquals("a", s);
    }
}
