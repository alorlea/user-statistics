package com.dw.userstatistics.auth;

import com.dw.userstatistics.db.api.UserAccessAPI;
import com.dw.userstatistics.db.entities.User;
import com.google.common.base.Optional;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;

/**
 * Created by Alberto on 2016-01-20.
 */
public class UserAuthenticator implements Authenticator<BasicCredentials,User> {
    private UserAccessAPI userAccessAPI;

    public UserAuthenticator(UserAccessAPI userAccessAPI){
        this.userAccessAPI = userAccessAPI;
    }
    @Override
    public Optional<User> authenticate(BasicCredentials basicCredentials) throws AuthenticationException {
        return getUserByCredentials(basicCredentials.getUsername(),basicCredentials.getPassword());
    }

    private Optional<User> getUserByCredentials(String username, String password) {
        User user = userAccessAPI.findUser(username);
        if(user ==null ||!user.getPassword().equals(password)){
            return Optional.absent();
        }else{
            return Optional.of(user);
        }
    }
}
