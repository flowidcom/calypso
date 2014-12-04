package com.flowid.refd.store;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"/spring/calypso.context.xml"})
public class JTestConfig {

    @Test
    public void testStartSpringConfig() {
        Assert.assertTrue(2 + 2 == 4);
    }
}
