package com.dw.userstatistics.db.api;

import com.dw.userstatistics.db.entities.User;

/**
 * Created by Alberto on 2016-01-19.
 */
public interface UserAccessAPI {
    public User createUser(User user);
    public User findUser(String username);
}
