package com.flowid.refd.store;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.flowid.store.MemRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/spring/calypso.context.xml" })
public class JTestGRepository {

    @Test
    public void testRepository() {
        MemRepository<String, String> repository = new MemRepository<String, String>() {
            @Override
            public String key(String s) {
                return s;
            }
        };

        repository.save("a");
        String s = repository.find("a");
        assertEquals("a", s);
    }
}
