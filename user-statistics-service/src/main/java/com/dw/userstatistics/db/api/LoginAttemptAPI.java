package com.dw.userstatistics.db.api;

import com.dw.userstatistics.db.entities.LoginAttempt;

import java.util.List;

/**
 * Created by Alberto on 2016-01-21.
 */
public interface LoginAttemptAPI {
    public List<LoginAttempt> findSuccesfulLoginAttempts(long userId);
    public LoginAttempt createLoginAttempt(LoginAttempt loginAttempt);
}
