package com.dw.userstatistics.db.dao;

import com.dw.userstatistics.db.api.UserAccessAPI;
import com.dw.userstatistics.db.entities.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
/**
 * Created by Alberto on 2016-01-19.
 */
public class UserDAO extends AbstractDAO<User> implements UserAccessAPI {


    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @Override
    public User createUser(User user) {
        return persist(user);
    }

    @Override
    public User findUser(String username) {
        User user = uniqueResult(namedQuery("findUserByUsername").setString("username",username));
        return user;
    }
}
