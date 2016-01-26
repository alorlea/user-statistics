package com.dw.userstatistics.resources;

import com.codahale.metrics.annotation.Timed;
import com.dw.userstatistics.db.api.LoginAttemptAPI;
import com.dw.userstatistics.db.entities.LoginAttempt;
import com.dw.userstatistics.db.entities.User;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Created by Alberto on 2016-01-21.
 */

@Path("/loginAttempts")
public class LoginAttemptsResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAttemptsResource.class);

    private LoginAttemptAPI loginAttemptAPI;

    public LoginAttemptsResource(LoginAttemptAPI loginAttemptAPI) {
        this.loginAttemptAPI = loginAttemptAPI;
    }

    @Path("/{id}")
    @GET
    @Timed
    @UnitOfWork
    @Produces(MediaType.APPLICATION_JSON)
    public Response getListOfLoginAttempts(@Auth User user, @PathParam("id") long userId){
        LOGGER.info("Received request to retrieve login attempts for user with userId: "+ userId);
        Response response;
        if(user.getId()==userId){
            LOGGER.debug(String.format("User with userId: %d is authorized to get the corresponding list",userId));
            List<LoginAttempt> attempts = loginAttemptAPI.findSuccesfulLoginAttempts(userId);
            response = Response.ok(attempts).build();
        }else{
            LOGGER.info(String.format("A user with userId: %d tried to attempt fetching data of another user with " +
                    "userId: %d",user.getId(),userId));
            response = Response.status(Response.Status.FORBIDDEN).build();
        }

        return response;
    }
}
