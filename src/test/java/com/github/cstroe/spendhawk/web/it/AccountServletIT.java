package com.github.cstroe.spendhawk.web.it;

import com.github.cstroe.spendhawk.testutil.web.DBUnitServlet;
import com.github.cstroe.spendhawk.web.AccountServlet;
import com.github.cstroe.spendhawk.web.BaseClientIT;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class AccountServletIT extends BaseClientIT {

    @Test
    @RunAsClient
    @InSequence(50)
    public void t0050_seed_database() throws Exception {
        response = connect(DBUnitServlet.class);
        assertResponseStatus(200, response);
    }

    @Test
    @RunAsClient
    @InSequence(100)
    public void t0100_can_connect() {
        connect200(AccountServlet.class, "id", 10l, "relDate", "currentMonth");
    }
}
