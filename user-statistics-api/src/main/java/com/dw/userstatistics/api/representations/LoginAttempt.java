package com.dw.userstatistics.api.representations;

import com.dw.userstatistics.json.CustomLoginAttemptDeserializer;
import com.dw.userstatistics.json.CustomLoginAttemptSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.joda.time.DateTime;

/**
 * Created by alberto on 2016-01-24.
 */
public class LoginAttempt {
    @JsonSerialize(using = CustomLoginAttemptSerializer.class)
    @JsonDeserialize(using = CustomLoginAttemptDeserializer.class)
    private DateTime dateTime;

    public LoginAttempt() {
    }

    public LoginAttempt(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoginAttempt)) return false;

        LoginAttempt that = (LoginAttempt) o;

        return dateTime != null ? dateTime.equals(that.dateTime) : that.dateTime == null;

    }

    @Override
    public int hashCode() {
        return dateTime != null ? dateTime.hashCode() : 0;
    }
}
