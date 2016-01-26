package com.dw.userstatistics.db.dao;

import com.dw.userstatistics.db.api.LoginAttemptAPI;
import com.dw.userstatistics.db.entities.LoginAttempt;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;

import java.util.List;

/**
 * Created by Alberto on 2016-01-21.
 */
public class LoginAttemptDAO extends AbstractDAO<LoginAttempt> implements LoginAttemptAPI {
    public LoginAttemptDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public List<LoginAttempt> findSuccesfulLoginAttempts(long userId) {
        return list(namedQuery("findLoginAttemptsByUserId").setLong(0,userId).setMaxResults(5));
    }

    @Override
    public LoginAttempt createLoginAttempt(LoginAttempt loginAttempt) {
        return persist(loginAttempt);
    }
}
