package com.github.cstroe.spendhawk.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.assertFalse;

@RunWith(SpringRunner.class)
@SpringBootTest
@ImportAutoConfiguration( { FlywayAutoConfiguration.class })
public class UserEntityIT {
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void testFindById() {
//        User.findById(1L).orElseThrow(Ex::userNotFound);
    }

    @Test
    public void testFindByIdWithBadId() {
//        assertFalse("Should not find non-existent user.",
//            User.findById(2L).isPresent());
//
//        assertFalse("Should not find null id",
//            User.findById(null).isPresent());
    }
}
