package com.dw.userstatistics.client;

import com.dw.userstatistics.api.representations.LoginAttempt;
import com.dw.userstatistics.api.representations.User;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixCommandProperties;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Created by alberto on 2016-05-17.
 *
 * An implementation of the login client which makes use of Hystrix Netflix library.
 */
public class UserLoginHystrixClient implements UserLoginTrackerAPI {

    private static final int EXECUTION_TIMEOUT_MILLIS = 1000;
    private final UserLoginTrackerAPI userLoginTrackerAPI;

    public UserLoginHystrixClient(final UserLoginTrackerAPI userLoginTrackerAPI) {
        this.userLoginTrackerAPI = userLoginTrackerAPI;
    }

    @Override
    public boolean registerNewUser(String username, String password) throws Exception {
        return new RegisterNewUserCommand(userLoginTrackerAPI,username,password).execute();
    }

    @Override
    public Optional<User> loginUser(String username, String password) throws IOException {
        return new LoginUserCommand(userLoginTrackerAPI,username,password).execute();
    }

    @Override
    public Optional<List<LoginAttempt>> listLoginAttempts(User user) throws IOException {
        return new ListLoginAttemptsCommand(userLoginTrackerAPI,user).execute();
    }

    private static class RegisterNewUserCommand extends HystrixCommand<Boolean>{
        private final UserLoginTrackerAPI userLoginTrackerAPI;
        private final String username;
        private final String password;

        protected RegisterNewUserCommand(final UserLoginTrackerAPI userLoginTrackerAPI, final String username,
                                        final String password){
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("UserLogin"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey("RegisterNewUser"))
                    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                            .withExecutionTimeoutInMilliseconds(EXECUTION_TIMEOUT_MILLIS)));

            this.userLoginTrackerAPI = userLoginTrackerAPI;
            this.username = username;
            this.password = password;
        }

        @Override
        protected Boolean run() throws Exception {
            return userLoginTrackerAPI.registerNewUser(username,password);
        }
    }

    private static class LoginUserCommand extends HystrixCommand<Optional<User>>{
        private final UserLoginTrackerAPI userLoginTrackerAPI;
        private final String username;
        private final String password;

        protected LoginUserCommand(final UserLoginTrackerAPI userLoginTrackerAPI, final String username,
                                         final String password){
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("UserLogin"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey("LoginUser"))
                    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                            .withExecutionTimeoutInMilliseconds(EXECUTION_TIMEOUT_MILLIS)));

            this.userLoginTrackerAPI = userLoginTrackerAPI;
            this.username = username;
            this.password = password;
        }

        @Override
        protected Optional<User> run() throws Exception {
            return userLoginTrackerAPI.loginUser(username,password);
        }
    }

    private static class ListLoginAttemptsCommand extends HystrixCommand<Optional<List<LoginAttempt>>>{
        private final UserLoginTrackerAPI userLoginTrackerAPI;
        private final User user;

        protected ListLoginAttemptsCommand(final UserLoginTrackerAPI userLoginTrackerAPI, final User user){
            super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("UserLogin"))
                    .andCommandKey(HystrixCommandKey.Factory.asKey("ListLoginAttempts"))
                    .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                            .withExecutionTimeoutInMilliseconds(EXECUTION_TIMEOUT_MILLIS)));

            this.userLoginTrackerAPI = userLoginTrackerAPI;
            this.user = user;
        }

        @Override
        protected Optional<List<LoginAttempt>> run() throws Exception {
            return userLoginTrackerAPI.listLoginAttempts(user);
        }
    }
}
