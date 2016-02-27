package com.github.cstroe.spendhawk.util;

import org.dbunit.DefaultDatabaseTester;
import org.dbunit.DefaultOperationListener;
import org.dbunit.IDatabaseTester;
import org.dbunit.database.DatabaseConnection;
import org.dbunit.database.IDatabaseConnection;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.io.InputStream;
import java.sql.SQLException;

/**
 * A base class for all integration tests.  Every test run, it will reset the
 * database with the seed data.
 */
public class BaseIT {

    protected Session currentSession;

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
                IDataSet seedDataset = new FlatXmlDataSetBuilder().setColumnSensing(true).build(seed);

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
    public void tearDown() {
        if(currentSession != null && currentSession.isOpen() && currentSession.getTransaction().isActive()) {
            currentSession.getTransaction().rollback();
        }

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

    public void startTransaction() {
        currentSession = HibernateUtil.getSessionFactory().getCurrentSession();
        currentSession.beginTransaction();
    }

    public void commitTransaction() {
        currentSession.getTransaction().commit();
    }

    public BeanManager getBeanManager()
    {
        try {
            InitialContext initialContext = new InitialContext();
            return (BeanManager) initialContext.lookup("java:comp/BeanManager");
        } catch (NamingException e) {
            return null;
        }
    }

    // CDI-based lookup of the bean.
    // See: http://dominikdorn.com/2010/04/cdi-weld-manual-bean-lookup/
    @SuppressWarnings({"unused", "unchecked"})
    public <T> T manualInject(Class<T> beanClass) {
        BeanManager bm = getBeanManager();
        Bean<T> bean = (Bean<T>) bm.getBeans(beanClass).iterator().next();
        CreationalContext<T> ctx = bm.createCreationalContext(bean);
        return (T) bm.getReference(bean, beanClass, ctx); // this could be inlined, but intentionally left this way
    }
}
