package com.dw.userstatistics.api.representations;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by Alberto on 2016-01-19.
 */
public class UserJSONRequest {

    private String username;
    private String password;

    public UserJSONRequest(){}

    public UserJSONRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @JsonProperty
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
    @JsonProperty
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserJSONRequest)) return false;

        UserJSONRequest userJSONRequest = (UserJSONRequest) o;

        if (username != null ? !username.equals(userJSONRequest.username) : userJSONRequest.username != null) return false;
        return password != null ? password.equals(userJSONRequest.password) : userJSONRequest.password == null;

    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
