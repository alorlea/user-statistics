package com.dw.userstatistics.db.entities;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static junit.framework.TestCase.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

/**
 * Created by Alberto on 2016-01-23.
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
        final LoginAttempt loginAttempt = new LoginAttempt(1,localDateTime);
        assertThat(MAPPER.writeValueAsString(loginAttempt)).isEqualTo(fixture("fixtures/loginAttempt.json"));
    }
    @Test
    public void deserializeToJSON() throws Exception{
        final LoginAttempt loginAttempt = new LoginAttempt(1,localDateTime);
        LoginAttempt deserialized = MAPPER.readValue(fixture("fixtures/loginAttempt.json"),LoginAttempt.class);
        deserialized.setUserId(1);//The data we show does not have the userId initially.
        assertTrue(loginAttempt.getUserId()==deserialized.getUserId());
        assertTrue(loginAttempt.getDateTime().equals(deserialized.getDateTime()));
    }

    @Test
    public void checkEqualsSameObjects(){
        final LoginAttempt loginAttempt = new LoginAttempt(1,localDateTime);
        final LoginAttempt loginAttempt1 = new LoginAttempt(1,localDateTime);
        assertTrue(loginAttempt.equals(loginAttempt1));
    }

    @Test
    public void checkEqualsNotSameUserId(){
        final LoginAttempt loginAttempt = new LoginAttempt(1,localDateTime);
        final LoginAttempt loginAttempt1 = new LoginAttempt(2,localDateTime);
        assertFalse(loginAttempt.equals(loginAttempt1));
    }
}