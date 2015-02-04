package com.github.cstroe.spendhawk.web.it;

import com.github.cstroe.spendhawk.entity.Account;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import org.dbunit.DefaultDatabaseTester;
import org.dbunit.DefaultOperationListener;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Integration tests having to do with transactions.
 */
@RunWith(Arquillian.class)
public class TransactionsIT {

    private Session currentSession;

    @Before
    public void setUp() {
        currentSession = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();
        currentSession.doWork(connection -> {
            try {
                // http://stackoverflow.com/questions/3526556/session-connection-deprecated-on-hibernate
                IDatabaseConnection dbConnection = new DatabaseConnection(connection);
                IDatabaseTester dbTester = new DefaultDatabaseTester(dbConnection);

                // http://stackoverflow.com/questions/2653322/getresourceasstream-not-loading-resource-in-webapp
                InputStream seed = Thread.currentThread().getContextClassLoader().getResourceAsStream("db/seed.xml");
                IDataSet seedDataset = new FlatXmlDataSetBuilder().build(seed);

                dbTester.setDataSet(seedDataset);
                // don't close the connection after we setup
                dbTester.setOperationListener(new DefaultOperationListener(){
                    @Override
                    public void operationSetUpFinished(IDatabaseConnection connection) {}

                    @Override
                    public void operationTearDownFinished(IDatabaseConnection connection) {}
                });
                dbTester.onSetup();
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        });
        transaction.commit();
    }

    @After
    public void tearDown() throws Exception {
        currentSession = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction transaction = currentSession.beginTransaction();
        currentSession.doWork(connection -> {
            try {
                // http://stackoverflow.com/questions/3526556/session-connection-deprecated-on-hibernate
                IDatabaseConnection dbConnection = new DatabaseConnection(connection);
                IDatabaseTester dbTester = new DefaultDatabaseTester(dbConnection);

                // http://stackoverflow.com/questions/2653322/getresourceasstream-not-loading-resource-in-webapp
                InputStream seed = Thread.currentThread().getContextClassLoader().getResourceAsStream("db/seed.xml");
                IDataSet seedDataset = new FlatXmlDataSetBuilder().build(seed);

                dbTester.setDataSet(seedDataset);
                // don't close the connection after we tear down
                dbTester.setOperationListener(new DefaultOperationListener(){
                    @Override
                    public void operationSetUpFinished(IDatabaseConnection connection) {}

                    @Override
                    public void operationTearDownFinished(IDatabaseConnection connection) {}
                });
                dbTester.onTearDown();
            } catch (Exception ex) {
                throw new SQLException(ex);
            }
        });
        transaction.commit();
    }

    @Test
    @InSequence(100)
    public void seedDatabaseWorks() {
        currentSession = HibernateUtil.getSessionFactory().getCurrentSession();
        currentSession.beginTransaction();
        List<Account> accounts = Account.findAll();
        assertEquals(1, accounts.size());
        Account firstAccount = accounts.get(0);
        assertEquals("Main Checking", firstAccount.getName());
        currentSession.getTransaction().commit();
    }

    @Test
    @InSequence(200)
    public void seedDatabaseWorks2() {
        currentSession = HibernateUtil.getSessionFactory().getCurrentSession();
        currentSession.beginTransaction();
        List<Account> accounts = Account.findAll();
        assertEquals(1, accounts.size());
        Account firstAccount = accounts.get(0);
        assertEquals("Main Checking", firstAccount.getName());
        currentSession.getTransaction().commit();
    }
}
