package com.dw.userstatistics;

import com.dw.userstatistics.api.representations.UserJSONRequest;
import com.dw.userstatistics.db.entities.LoginAttempt;
import com.dw.userstatistics.db.entities.User;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by Alberto on 2016-01-19.
 */
public class IntegrationTest {
    private static final String TMP_FILE = createTempFile();
    private static final String CONFIG_PATH = ResourceHelpers.resourceFilePath("test-configuration.yaml");


    @ClassRule
    public static final DropwizardAppRule<UserStatisticsConfiguration> RULE = new DropwizardAppRule<>
            (UserStatisticsApplication.class, CONFIG_PATH,
                    ConfigOverride.config("database.url","jdbc:h2:" + TMP_FILE));

    private Client client;


    @Before
    public void setUp() throws Exception {

        client = ClientBuilder.newClient();

    }


    @After
    public void tearDown() throws Exception {
        client.close();
    }

    private static String createTempFile() {
        try {
            return File.createTempFile("test-example", null).getAbsolutePath();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void testPostUser() throws Exception{
        final UserJSONRequest userJSONRequest = new UserJSONRequest("pepe","josete");
        final Response response = client.target("http://localhost:" + RULE.getLocalPort() +"/register")
                .request().post(Entity.json(userJSONRequest));
        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
    }

    @Test
    public void testLoginUser() throws Exception{
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("juan", "admin123");
        client.register(feature);
        //insert first the user
        final UserJSONRequest userJSONRequest = new UserJSONRequest("juan","admin123");
        final Response response = client.target("http://localhost:" + RULE.getLocalPort() +"/register")
                .request().post(Entity.json(userJSONRequest));

        assertThat(response.getStatus()).isEqualTo(Response.Status.CREATED.getStatusCode());
        //try to login;
        final User user= client.target("http://localhost:"+RULE.getLocalPort() + "/login").request().get(User.class);
        assertTrue(user.getUsername().equals("juan"));
        assertTrue(user.getPassword().equals("admin123"));
    }

    @Test
    public void testNonAccesibleUser() throws Exception{
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("nonexistent", "admin123");
        client.register(feature);

        final Response response= client.target("http://localhost:"+RULE.getLocalPort() + "/login").request().get();

        assertTrue(response.getStatus()==401);
    }

    @Test
    public void testAccesingLoginAttemptsSecureResource() throws Exception{
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("test1", "test123");
        client.register(feature);
        //register the user
        final UserJSONRequest userJSONRequest = new UserJSONRequest("test1","test123");
        final Response response = client.target("http://localhost:" + RULE.getLocalPort() +"/register")
                .request().post(Entity.json(userJSONRequest));

        //login user
        final User user= client.target("http://localhost:"+RULE.getLocalPort() + "/login").request().get(User.class);

        final long userId = user.getId();
        //use the id to get the list of attempts
        final List<LoginAttempt> listOfAttempts = client.target("http://localhost:" +RULE.getLocalPort() +
                "/loginAttempts/"+userId)
                .request().get(new GenericType<List<LoginAttempt>>(){});

        assertTrue(listOfAttempts.size()==1);

    }

    @Test
    public void testAccesingLoginAttemptsSecureResourceNotFromUser() throws Exception{
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic("test2", "test123");
        client.register(feature);
        //register the user
        final UserJSONRequest userJSONRequest = new UserJSONRequest("test2","test123");
        final Response response = client.target("http://localhost:" + RULE.getLocalPort() +"/register")
                .request().post(Entity.json(userJSONRequest));

        //login user
        final User user= client.target("http://localhost:"+RULE.getLocalPort() + "/login").request().get(User.class);

        final long userId = 100;
        //use the id to get the list of attempts
        final Response response1 = client.target("http://localhost:" +RULE.getLocalPort() +
                "/loginAttempts/"+userId)
                .request().get();

        assertTrue(response1.getStatus()==403);

    }
}
