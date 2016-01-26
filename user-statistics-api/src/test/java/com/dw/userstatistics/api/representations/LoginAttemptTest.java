package com.dw.userstatistics.api.representations;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Alberto on 2016-01-25.
 */
public class LoginAttemptTest {
    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
    private DateTime localDateTime;

    @Before
    public void setup(){
        localDateTime = new DateTime("2004-12-14T06:39:45.618");
    }

    @Test
    public void serializeToJSON() throws Exception{
        final LoginAttempt loginAttempt = new LoginAttempt(localDateTime);
        assertThat(MAPPER.writeValueAsString(loginAttempt)).isEqualTo(fixture("fixtures/loginAttempt.json"));
    }
    @Test
    public void deserializeToJSON() throws Exception{
        final LoginAttempt loginAttempt = new LoginAttempt(localDateTime);
        LoginAttempt deserialized = MAPPER.readValue(fixture("fixtures/loginAttempt.json"),LoginAttempt.class);
        assertTrue(loginAttempt.getDateTime().equals(deserialized.getDateTime()));
    }

    @Test
    public void checkEqualsSameObjects(){
        final LoginAttempt loginAttempt = new LoginAttempt(localDateTime);
        final LoginAttempt loginAttempt1 = new LoginAttempt(localDateTime);
        assertTrue(loginAttempt.equals(loginAttempt1));
    }
}