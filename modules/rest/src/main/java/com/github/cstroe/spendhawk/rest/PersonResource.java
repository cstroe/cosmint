package com.github.cstroe.spendhawk.rest;

import com.github.cstroe.spendhawk.rest.model.PersonBean;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("person")
public class PersonResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public PersonBean createPerson(PersonBean newPerson) {
        return newPerson;
    }
}
