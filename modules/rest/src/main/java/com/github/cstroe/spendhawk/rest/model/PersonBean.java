package com.github.cstroe.spendhawk.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class PersonBean {
    private String login;

    @SuppressWarnings("unused") // for MOXy
    public PersonBean() {}

    public PersonBean(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
