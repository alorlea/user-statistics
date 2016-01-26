package com.dw.userstatistics.api.representations;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by alberto on 2016-01-24.
 */
public class UserTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void serializeToJSON() throws Exception{
        final User user = new User(1,"pepe","josete");
        assertThat(MAPPER.writeValueAsString(user)).isEqualTo(fixture("fixtures/createdUser.json"));
    }

    @Test
    public void deserializeFromJSON() throws Exception{
        final User user = new User(1,"pepe","josete");
        assertThat(MAPPER.readValue(fixture("fixtures/createdUser.json"),User.class));
    }
}
