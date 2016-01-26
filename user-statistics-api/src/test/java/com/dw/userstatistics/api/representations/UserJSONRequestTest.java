package com.dw.userstatistics.api.representations;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import org.junit.Test;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Alberto on 2016-01-19.
 */
public class UserJSONRequestTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Test
    public void serializeToJSON() throws Exception{
        final UserJSONRequest userJSONRequest = new UserJSONRequest("pepe","josete");
        assertThat(MAPPER.writeValueAsString(userJSONRequest)).isEqualTo(fixture("fixtures/user.json"));
    }

    @Test
    public void deserializeFromJSON() throws Exception{
        final UserJSONRequest userJSONRequest = new UserJSONRequest("pepe","josete");
        assertThat(MAPPER.readValue(fixture("fixtures/user.json"),UserJSONRequest.class));
    }
}