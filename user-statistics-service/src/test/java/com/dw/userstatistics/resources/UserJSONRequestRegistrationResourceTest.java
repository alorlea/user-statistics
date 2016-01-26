package com.dw.userstatistics.resources;

import com.dw.userstatistics.api.representations.UserJSONRequest;
import com.dw.userstatistics.db.api.UserAccessAPI;
import com.dw.userstatistics.db.entities.User;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Alberto on 2016-01-19.
 */
public class UserJSONRequestRegistrationResourceTest {
    private static UserAccessAPI userAccessAPI=mock(UserAccessAPI.class);;

    @ClassRule
    public static final ResourceTestRule resource = ResourceTestRule.builder().addResource(new
            UserRegistrationResource(userAccessAPI)).build();

    @Before
    public void setup(){
    }
    @Test
    public void createNewUser(){
        UserJSONRequest userJSONRequest = new UserJSONRequest("pepe","josete");
        User user = new User("pepe","josete");

        when(userAccessAPI.createUser(user)).thenReturn(user);
        Response response = resource.client().target("/register").request(MediaType.APPLICATION_JSON_TYPE).post
                (Entity.json(userJSONRequest));
        assertEquals(response.getStatus(),201);
    }
}