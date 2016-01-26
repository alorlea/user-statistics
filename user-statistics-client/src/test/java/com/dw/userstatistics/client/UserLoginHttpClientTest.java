package com.dw.userstatistics.client;

import com.dw.userstatistics.api.representations.LoginAttempt;
import com.dw.userstatistics.api.representations.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Alberto on 2016-01-25.
 */
public class UserLoginHttpClientTest {

    private UserLoginTrackerAPI userLoginTrackerAPI;
    private URI baseURI;
    private CloseableHttpClient closeableHttpClient;
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Before
    public void setup() throws URISyntaxException {
        baseURI = new URI("http://localhost:8080");
        closeableHttpClient = mock(CloseableHttpClient.class);
        userLoginTrackerAPI = new UserLoginHttpClient(baseURI,closeableHttpClient);
    }

    @Test
    public void testUserRegistration() throws Exception {
        //Test data
        Boolean isCreated = true;
        String username = "pepe";
        String password = "jose";

        //Mocking interaction
        when(closeableHttpClient.execute(any(HttpPost.class),any(ResponseHandler.class))).thenReturn(isCreated);

        //Actual test
        boolean result = userLoginTrackerAPI.registerNewUser(username,password);

        assertTrue(result);
    }

    @Test
    public void testLoginUser() throws Exception{
        //Test data
        String username = "pepe";
        String password = "jose";
        Optional<String> response = Optional.of("{\"username\":\"pepe\",\"password\":\"jose\"}");

        //Mocking interaction
        when(closeableHttpClient.execute(any(HttpGet.class),any(ResponseHandler.class),any(HttpClientContext.class))).thenReturn(response);

        //Actual test
        Optional<User> user = userLoginTrackerAPI.loginUser(username,password);

        assertTrue(user.isPresent());
        User userFromOptional = user.get();

        assertTrue(username.equals(userFromOptional.getUsername()));
        assertTrue(password.equals(userFromOptional.getPassword()));
    }

    @Test
    public void testFetchLoginAttempts() throws Exception{
        //Logged in user information
        long userId = 1;
        String username = "alberto";
        String password = "josete";
        User user = new User(userId,username,password);

        //Generate test list of attempts

        List<LoginAttempt> listFromServer = new ArrayList<>();
        LoginAttempt attempt1 = new LoginAttempt(DateTime.now());
        LoginAttempt attempt2 = new LoginAttempt(DateTime.now());
        listFromServer.add(attempt1);
        listFromServer.add(attempt2);

        //Transformations into JSON and optional
        String listInJSON = MAPPER.writeValueAsString(listFromServer);
        Optional<String> optionalFromJSONString = Optional.of(listInJSON);

        //Mocking interaction
        when(closeableHttpClient.execute(any(HttpGet.class),any(ResponseHandler.class),any(HttpClientContext.class))).thenReturn(optionalFromJSONString);

        //Actual test
        Optional<List<LoginAttempt>> optionalResponseFromServer = userLoginTrackerAPI.listLoginAttempts(user);

        assertTrue(optionalResponseFromServer.isPresent());
        List<LoginAttempt> listFromOptional = optionalResponseFromServer.get();

        assertEquals(listFromServer.size(),listFromOptional.size());
        assertEquals(attempt1,listFromOptional.get(0));
        assertEquals(attempt2,listFromOptional.get(1));

    }

}