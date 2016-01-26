package com.dw.userstatistics.client;

import com.dw.userstatistics.api.representations.LoginAttempt;
import com.dw.userstatistics.api.representations.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by alberto on 2016-01-24.
 */
public interface UserLoginTrackerAPI {

    public boolean registerNewUser(String username, String password) throws Exception;
    public Optional<User> loginUser(String username, String password) throws IOException;
    public Optional<List<LoginAttempt>> listLoginAttempts(User user) throws IOException;
}
