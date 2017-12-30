package com.github.cstroe.spendhawk.bean;

import com.github.cstroe.spendhawk.dao.UserDao;
import com.github.cstroe.spendhawk.util.HibernateUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * A bean to keep user management business logic.
 */
@Service
public class UserManagerBean {

    private String message;

    /**
     * @return A user facing error message.
     */
    public String getMessage() {
        return message;
    }

    public Optional<UserDao> addUser(String username) {
        if(username == null || StringUtils.isBlank(username)) {
            message = "UserDao name is blank.";
            return Optional.empty();
        }

        UserDao newUser = new UserDao(-1l, username, Lists.newArrayList());

        Session currentSession = HibernateUtil.getSessionFactory().getCurrentSession();
        try {
            currentSession.beginTransaction();
            currentSession.save(newUser);
            currentSession.getTransaction().commit();
            return Optional.of(newUser);
        } catch(Exception ex) {
            currentSession.getTransaction().rollback();
            message = ex.getMessage();
            return Optional.empty();
        }
    }
}
