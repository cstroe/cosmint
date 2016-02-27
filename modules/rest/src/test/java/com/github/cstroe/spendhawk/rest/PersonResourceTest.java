package com.github.cstroe.spendhawk.rest;

import com.github.cstroe.spendhawk.rest.model.PersonBean;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;

public class PersonResourceTest extends JerseyTest {
    @Override
    protected Application configure() {
        //enable(TestProperties.LOG_TRAFFIC);
        //enable(TestProperties.DUMP_ENTITY);
        return new JerseyApplication();
    }

    @Test
    public void test() throws Exception {
        final WebTarget target = target("person");
        final PersonBean personBean = target
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.entity(new PersonBean("cosmin"), MediaType.APPLICATION_JSON_TYPE), PersonBean.class);

        assertEquals("cosmin", personBean.getLogin());
    }
}
