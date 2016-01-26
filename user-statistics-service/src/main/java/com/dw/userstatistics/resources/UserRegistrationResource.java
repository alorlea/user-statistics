package com.dw.userstatistics.resources;

import com.codahale.metrics.annotation.Timed;
import com.dw.userstatistics.api.representations.UserJSONRequest;
import com.dw.userstatistics.db.api.UserAccessAPI;
import com.dw.userstatistics.db.entities.User;
import io.dropwizard.hibernate.UnitOfWork;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by Alberto on 2016-01-19.
 */

@Path("/register")
@Produces(MediaType.APPLICATION_JSON)
public class UserRegistrationResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationResource.class);

    private UserAccessAPI userAccessAPI;

    public UserRegistrationResource(UserAccessAPI userAccessAPI) {
        this.userAccessAPI = userAccessAPI;
    }

    @POST
    @UnitOfWork
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNewUser(UserJSONRequest userJSONRequest){
        LOGGER.info("Received request to create a new user");
        //Do something to store the value somewhere
        User user = new User(userJSONRequest.getUsername(), userJSONRequest.getPassword());
        userAccessAPI.createUser(user);

        LOGGER.debug("Received username: " + userJSONRequest.getUsername() +" with password: "+ userJSONRequest.getPassword());
        return Response.status(201).build();
    }
}
