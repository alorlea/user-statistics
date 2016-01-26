package com.dw.userstatistics.resources;

import com.codahale.metrics.annotation.Timed;
import com.dw.userstatistics.db.api.LoginAttemptAPI;
import com.dw.userstatistics.db.entities.LoginAttempt;
import com.dw.userstatistics.db.entities.User;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Alberto on 2016-01-20.
 */

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
public class LoginResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginResource.class);

    private LoginAttemptAPI loginAttemptAPI;

    public LoginResource(LoginAttemptAPI loginAttemptAPI) {
        this.loginAttemptAPI = loginAttemptAPI;
    }

    @GET
    @UnitOfWork
    @Timed
    @Produces(MediaType.APPLICATION_JSON)
    public Response authorizeUser(@Auth User user){
        LOGGER.info("Got succesful login attempt from user with userID: "+ user.getId());
        DateTime date = DateTime.now();
        LoginAttempt loginAttempt = new LoginAttempt(user.getId(),date);
        loginAttemptAPI.createLoginAttempt(loginAttempt);
        return Response.accepted(user).build();
    }
}
